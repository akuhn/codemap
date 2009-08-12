package org.codemap.resources;

import java.util.Collection;

import org.codemap.util.JobValue;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.ReferenceValue;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;


public class MapValues {

    private String name;

    private final Value<Collection<String>> projects;
    private final Value<Collection<String>> extensions;
    
    public final Value<Integer> mapSize;

    public final JobValue<Collection<String>> elements;
    public final JobValue<LatentSemanticIndex> index;
    public final JobValue<Configuration> configuration;
    public final JobValue<MapInstance> mapInstance;


    public MapValues(String name, Collection<String> allProjects, 
            Collection<String> allExtensions, Configuration initialConfiguration) {
        this.setName(name);
        mapSize = new ReferenceValue<Integer>(0);
        projects = new ReferenceValue<Collection<String>>(allProjects);
        extensions = new ReferenceValue<Collection<String>>(allExtensions);
        elements = new ComputeElementsTask(name, projects, extensions);
        index = new ComputeIndexTask(name, elements);
        configuration = new ComputeConfigurationTask(name, index)
                .initialConfiguration(initialConfiguration);
        mapInstance = new ComputeMapInstanceTask(name, mapSize, index, configuration);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
