package org.codemap.resources;

import java.util.Collection;

import org.codemap.CodemapCore;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.CollectionValue;
import ch.akuhn.values.Value;
import ch.akuhn.values.Values;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValueBuilder;


public class EclipseMapValuesBuilder extends MapValueBuilder {

    private Value<Collection<String>> projects;
    private Value<Collection<String>> fileExtensions;
    private String name;

    public Value<Collection<String>> projectsValue() {
        if (projects == null) projects = Values.of(null);
        return projects;
    }
    
    /**
     * represents the file extensions that will be processed.
     */
    public Value<Collection<String>> extensionsValue() {
        if (fileExtensions == null) fileExtensions = Values.of(null);
        return fileExtensions;
    }

    @Override
    public Value<Collection<String>> elementsValue() {
        return new ComputeElementsTask(projectsValue(), extensionsValue());
    }
    
    @Override
    public Value<LatentSemanticIndex> indexValue(Value<Collection<String>> elements) {
        return new ComputeEclipseIndexTask(elements);
    }

    public EclipseMapValuesBuilder setName(String string) {
        this.name = string;
        return this;
    }

    public EclipseMapValuesBuilder setProjects(Collection<String> projects) {
        projectsValue().setValue(projects);
        return this;
    }
    
    /**
     * set the file extensions that will be processed.
     */    
    public EclipseMapValuesBuilder setFileExtensions(Collection<String> extensions) {
        extensionsValue().setValue(extensions);
        return this;
    }
    
    @Override
    public EclipseMapValuesBuilder setInitialConfiguration(Configuration initialConfiguration) {
        super.setInitialConfiguration(initialConfiguration);
        return this;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public MapSelection youAreHereSelection() {
        // TODO remove dependents when disposing a MapValues instance!
        return CodemapCore.getPlugin().getYouAreHereSelection();
    }
    
    @Override
    public MapSelection currentSelectionValue() {
        // TODO remove dependents when disposing a MapValues instance!
        return CodemapCore.getPlugin().getCurrentSelection();
    }
    
    @Override
    public MapSelection openFilesSelectionValue() {
        // TODO remove dependents when disposing a MapValues instance!
        return CodemapCore.getPlugin().getOpenFilesSelection();
    }

    public CollectionValue<MapSelection> selectionsValue(MapSelection... selections) {
        MapSelectionsValue value = new MapSelectionsValue();
        for(MapSelection each: selections) {
            value.add(each);
        }
        return value;
    }

    public CollectionValue<Value> addonsValue() {
        return new CollectionValue<Value>();
    }
    
}
