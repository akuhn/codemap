package org.codemap.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.util.JobMonitor;
import org.codemap.util.JobValue;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.Files;
import ch.akuhn.values.Value;
import ch.akuhn.values.ValueChangedListener;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.util.MapScheme;


public class NewMapResource {

	private String name;

	private Value<Collection<String>> fProjects;
	private Value<Collection<String>> fExtensions;
	private Value<Integer> fMapSize;

	private JobValue<Collection<String>> fElements;
	private JobValue<LatentSemanticIndex> fIndex;
	private JobValue<Configuration> fConfiguration;
	private JobValue<MapInstance> fMapInstance;

	public NewMapResource(String name, Collection<String> projects, Collection<String> extensions) {
		this.name = name;
		this.initializeValues();
		this.fProjects.setValue(projects);
		this.fExtensions.setValue(extensions);
	}

	private void initializeValues() {
		fMapSize = new Value<Integer>(0);
		fProjects = new Value<Collection<String>>();
		fExtensions = new Value<Collection<String>>();
		fElements = new JobValue<Collection<String>>("Compute elements", fProjects, fExtensions) {
			@Override
			protected Collection<String> computeValue(JobMonitor monitor) {
				return computeElements(monitor);
			}

		};
		fIndex = new JobValue<LatentSemanticIndex>("Compute index",fElements) {
			@Override
			protected LatentSemanticIndex computeValue(JobMonitor monitor) {
				return computeIndex(monitor);
			}
		};
		fConfiguration = new JobValue<Configuration>("Compute configuration",fIndex) {
			@Override
			protected Configuration computeValue(JobMonitor monitor) {
				return computeConfiguration(monitor);
			}
		};
		fMapInstance = new JobValue<MapInstance>("Compute map instance", fMapSize, fIndex, fConfiguration) {
			@Override
			protected MapInstance computeValue(JobMonitor monitor) {
				return computeMapInstance(monitor);
			}
		};
	}

	private Collection<String> computeElements(JobMonitor monitor) {
		Collection<String> projects = monitor.nextArgument();
		final Collection<String> extensions = monitor.nextArgument();
		final Collection<String> result = new HashSet<String>();
		for (String path: projects) {
			IResource project = Resources.asResource(path);
			try {
				project.accept(new IResourceVisitor() {
					@Override
					public boolean visit(IResource resource) throws CoreException {
						if (resource.getType() == IResource.FILE) {
							for (String pattern: extensions) {
								if (Files.match(pattern, resource.getName())) result.add(Resources.asPath(resource));
							}
						}
						return true;
					}
				});
			} catch (CoreException ex) {
				throw new RuntimeException(ex);
			}
		}
		return new ArrayList<String>(result);
	}

	private LatentSemanticIndex computeIndex(JobMonitor monitor) {
		Collection<String> elements = monitor.nextArgument();
		CorpusBuilder builder = Hapax.newCorpus()
		.ignoreCase()
		.useCamelCaseScanner()
		.rejectRareTerms()
		.rejectStopwords()
		.latentDimensions(25)
		.useTFIDF();
		for (String path: elements) {
			try {
				IResource resource = Resources.asResource(path);
				if (resource.getType() != IResource.FILE) continue;
				InputStream stream = ((IFile) resource).getContents();
				builder.addDocument(path, stream);
			} catch (CoreException ex) {
				throw new RuntimeException(ex);
			}
		}
		return builder.makeTDM().createIndex();
	}

	private Configuration computeConfiguration(JobMonitor monitor) {
		LatentSemanticIndex index = monitor.nextArgument();
		return Meander.builder().addCorpus(index).makeMap();
	}

	private MapInstance computeMapInstance(JobMonitor monitor) {
		int size = monitor.nextArgument();
		final LatentSemanticIndex index = monitor.nextArgument();
		Configuration configuration = monitor.nextArgument();
		return configuration.withSize(size, new MapScheme<Double>() {
			@Override
			public Double forLocation(Point location) {
				return Math.sqrt(index.getTermCount(location.getDocument()));

			}
		});
	}

	public int getMapSize() {
		return fMapSize.getValue();
	}

	public void setMapSize(int mapSize) {
		this.fMapSize.setValue(mapSize);
	}

	public String getName() {
		return name;
	}

	public Iterable<String> getProjects() {
		return fProjects.awaitValue();
	}

	public Iterable<String> getExtensions() {
		return fExtensions.awaitValue();
	}

	public Iterable<String> getElements() {
		return fElements.awaitValue();
	}

	public LatentSemanticIndex getIndex() {
		return fIndex.awaitValue();
	}

	public Configuration getConfiguration() {
		return fConfiguration.awaitValue();
	}

	public MapInstance getMapInstance() {
		return fMapInstance.awaitValue();
	}

	public void addMapInstanceListener(ValueChangedListener dependent) {
		fMapInstance.addDependent(dependent);
	}

}
