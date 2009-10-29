package org.codemap.resources;

import java.util.Collection;

import ch.akuhn.values.CollectionValue;
import ch.akuhn.values.MapValue;
import ch.akuhn.values.Value;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;


public class EclipseMapValues extends MapValues {

    public final Value<Collection<String>> projects;
    public final Value<Collection<String>> extensions;
    public final CollectionValue<MapSelection> selections;
    // TODO deif: i'm here ... so continue here please
//    public final MapValue<String, Value<?>> addons;
    
    public EclipseMapValues(EclipseMapValuesBuilder make) {
        super(make);
        
        projects = make.projectsValue();
        extensions = make.extensionsValue();
        selections = make.selectionsValue(currentSelection, openFilesSelection, youAreHereSelection);
//        addons = make.addonsValue();
    }

}
