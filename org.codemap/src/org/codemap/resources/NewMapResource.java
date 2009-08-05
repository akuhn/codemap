package org.codemap.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.Files;
import ch.akuhn.values.ComputedValue;
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

	private ComputedValue<Collection<String>> fElements;
	private ComputedValue<LatentSemanticIndex> fIndex;
	private ComputedValue<Configuration> fConfiguration;
	private ComputedValue<MapInstance> fMapInstance;

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
		fElements = new ComputedValue<Collection<String>>(fProjects, fExtensions) {
			@Override
			protected Collection<String> computeValue() {
				return computeElements(this.<Collection<String>>arg(0), this.<Collection<String>>arg(1));
			}
		};
		fIndex = new ComputedValue<LatentSemanticIndex>(fElements) {
			@Override
			protected LatentSemanticIndex computeValue() {
				return computeIndex(this.<Collection<String>>arg(0));
			}
		};
		fConfiguration = new ComputedValue<Configuration>(fIndex) {
			@Override
			protected Configuration computeValue() {
				return computeConfiguration(this.<LatentSemanticIndex>arg(0));
			}
		};
		fMapInstance = new ComputedValue<MapInstance>(fMapSize, fIndex, fConfiguration) {
			@Override
			protected MapInstance computeValue() {
				return computeMapInstance(
						this.<Integer>arg(0),
						this.<LatentSemanticIndex>arg(1),
						this.<Configuration>arg(2));
			}
		};
	}

	private Collection<String> computeElements(Collection<String> projects, final Collection<String> extensions) {
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

	private LatentSemanticIndex computeIndex(Collection<String> elements) {
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
	
	private Configuration computeConfiguration(LatentSemanticIndex index) {
		return Meander.builder().addCorpus(index).makeMap();
	}

	private MapInstance computeMapInstance(int size, final LatentSemanticIndex index, Configuration configuration) {
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
		return fProjects.getValue();
	}

	public Iterable<String> getExtensions() {
		return fExtensions.getValue();
	}

	public Iterable<String> getElements() {
		return fElements.getValue();
	}

	public LatentSemanticIndex getIndex() {
		return fIndex.getValue();
	}

	public Configuration getConfiguration() {
		return fConfiguration.getValue();
	}

	public MapInstance getMapInstance() {
		return fMapInstance.getValue();
	}

	public void addMapInstanceListener(ValueChangedListener dependent) {
		fMapInstance.addDependent(dependent);
	}

}
