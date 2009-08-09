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
import ch.akuhn.values.ReferenceValue;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.util.MapScheme;


public class NewMapResource {

    private String name;

    private final Value<Collection<String>> projects;
    private final Value<Collection<String>> extensions;
    private final Value<Configuration> fInitialConfiguration;
    
    public final Value<Integer> mapSize;

    public final JobValue<Collection<String>> elements;
    public final JobValue<LatentSemanticIndex> index;
    public final JobValue<Configuration> configuration;
    public final JobValue<MapInstance> mapInstance;


    public NewMapResource(String name, Collection<String> allProjects, 
            Collection<String> allExtensions, Configuration configuration2) {
        this.setName(name);
        mapSize = new ReferenceValue<Integer>(0);
        projects = new ReferenceValue<Collection<String>>(allProjects);
        extensions = new ReferenceValue<Collection<String>>(allExtensions);
        fInitialConfiguration = new ReferenceValue<Configuration>(configuration2 == null
                ? new Configuration() : configuration2);
        elements = new JobValue<Collection<String>>("Compute elements", projects, extensions) {
            @Override
            protected Collection<String> computeValue(JobMonitor monitor) {
                return computeElements(monitor);
            }

        };
        index = new JobValue<LatentSemanticIndex>("Compute index", elements) {
            @Override
            protected LatentSemanticIndex computeValue(JobMonitor monitor) {
                return computeIndex(monitor);
            }
        };
        configuration = new JobValue<Configuration>("Compute configuration", index, fInitialConfiguration) {
            @Override
            protected Configuration computeValue(JobMonitor monitor) {
                return computeConfiguration(monitor);
            }
        };
        mapInstance = new JobValue<MapInstance>("Compute map instance", mapSize, index, configuration) {
            @Override
            protected MapInstance computeValue(JobMonitor monitor) {
                return computeMapInstance(monitor);
            }
        };
    }

    private static Collection<String> computeElements(JobMonitor monitor) {
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

    private static LatentSemanticIndex computeIndex(JobMonitor monitor) {
        Collection<String> elements = monitor.nextArgument();
        monitor.begin(elements.size());
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
            monitor.worked(1);
        }
        monitor.done();
        return builder.makeTDM().createIndex();
    }

    private static Configuration computeConfiguration(JobMonitor monitor) {
        LatentSemanticIndex index = monitor.nextArgument();
        Configuration initialConfiguration = monitor.nextArgument();
        return Meander.builder().addCorpus(index).makeMap(initialConfiguration);
    }

    private static MapInstance computeMapInstance(JobMonitor monitor) {
        int size = monitor.nextArgument();
        final LatentSemanticIndex index = monitor.nextArgument();
        Configuration configuration = monitor.nextArgument();
        return configuration.withSize(size, new MapScheme<Double>() {
            @Override
            public Double forLocation(Point location) {
                return Math.sqrt(index.getDocumentLength(location.getDocument()));

            }
        });
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
