package org.codemap.resources;

import java.util.Collection;
import java.util.List;

import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.map.MapValues;


public class EclipseMapValues extends MapValues {

    public final Value<Collection<String>> projects;
    public final Value<Collection<String>> extensions;
    
    public EclipseMapValues(EclipseMapValuesBuilder make) {
        super(make);
        
        projects = make.projectsValue();
        extensions = make.extensionsValue();
        
    }

    public EclipseMapValues(String string, List<String> asList, List<String> asList2, Configuration readPreviousMapState) {
        this(new EclipseMapValuesBuilder()
            .setName(string)
            .setProjects(asList)
            .setExtensions(asList2)
            .setInitialConfiguration(readPreviousMapState));
    }
    
}
