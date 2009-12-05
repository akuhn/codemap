package org.codemap.resources;

import java.util.Collection;
import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.Configuration;
import org.codemap.DigitalElevationModel;
import org.codemap.HillShading;
import org.codemap.Labeling;
import org.codemap.MapInstance;
import org.codemap.MapSelection;
import org.codemap.tasks.ComputeBackgroundTask;
import org.codemap.tasks.ComputeConfigurationTask;
import org.codemap.tasks.ComputeHillShadingTask;
import org.codemap.tasks.ComputeLabelingTask;
import org.codemap.tasks.ComputeMapInstanceTask;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;
import org.eclipse.swt.graphics.Image;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.BooleanValue;
import ch.akuhn.values.CollectionValue;
import ch.akuhn.values.IntegerValue;
import ch.akuhn.values.ReferenceValue;
import ch.akuhn.values.Value;
import ch.akuhn.values.Values;

public class MapValueBuilder {

    private Configuration initialConfiguration;
    private Value<Collection<String>> projects;
    private Value<Collection<String>> fileExtensions;
    private String name;
    private BooleanValue showTestsValue;
    
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

    public MapValueBuilder setInitialConfiguration(Configuration initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
        return this;
    }

    public IntegerValue mapSizeValue() {
        return new IntegerValue(512);
    }

    public Value<Image> backgroundValue(Value<MapInstance> mapInstance, Value<DigitalElevationModel> elevationModel, Value<HillShading> shading, Value<MapScheme<MColor>> colors) {
        return new ComputeBackgroundTask(mapInstance, elevationModel, shading, colors);
    }

    public Value<Collection<String>> elementsValue() {
        return new ComputeElementsTask(projectsValue(), extensionsValue());
    }

    public Value<MapScheme<Boolean>> hillsValue() {
        return new ReferenceValue<MapScheme<Boolean>>();
    }

    public Value<MapScheme<MColor>> colorsValue() {
        return new ReferenceValue<MapScheme<MColor>>();
    }

    public Value<MapScheme<String>> labelsValue() {
        return new ReferenceValue<MapScheme<String>>();
    }

    public Value<LatentSemanticIndex> indexValue(Value<Collection<String>> elements) {
        return new ComputeEclipseIndexTask(elements);
    }

    public Value<Configuration> configurationValue(Value<LatentSemanticIndex> index) {
        return new ComputeConfigurationTask(index)
                .initialConfiguration(initialConfiguration);
    }

    public Value<MapInstance> mapInstanceValue(Value<Integer> mapSize, Value<LatentSemanticIndex> index, Value<Configuration> configuration) {
        return new ComputeMapInstanceTask(mapSize, index, configuration);
    }

    public Value<DigitalElevationModel> elevationModelValue(Value<MapInstance> mapInstance, Value<MapScheme<Boolean>> hills) {
        return new ComputeFilteredElevationTask(mapInstance, hills, showTestsValue());
    }

    public Value<HillShading> hillShadingValue(Value<MapInstance> mapInstance, 
            Value<DigitalElevationModel> elevationModel) {
        return new ComputeHillShadingTask(mapInstance, elevationModel);
    }

    public Value<Labeling> labelingValue(Value<MapInstance> mapInstance, Value<MapScheme<String>> labels) {
        return new ComputeLabelingTask(mapInstance, labels);
    }

    public MapSelection currentSelectionValue() {
        // TODO remove dependents when disposing a MapValues instance!
        return CodemapCore.getPlugin().getCurrentSelection();
    }

    public MapSelection openFilesSelectionValue() {
        // TODO remove dependents when disposing a MapValues instance!
        return CodemapCore.getPlugin().getOpenFilesSelection();
    }

    public MapSelection youAreHereSelection() {
        // TODO remove dependents when disposing a MapValues instance!
        return CodemapCore.getPlugin().getYouAreHereSelection();
    }
    
    public MapValueBuilder setName(String string) {
        this.name = string;
        return this;
    }
    
    public String getName() {
        return name;
    }    
    
    public MapValueBuilder setProjects(Collection<String> projects) {
        projectsValue().setValue(projects);
        return this;
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

    public Value<Boolean> showTestsValue() {
        if (showTestsValue == null) {
            showTestsValue = new BooleanValue(true);
        }
        return showTestsValue;
    }

    public MapValueBuilder setFileExtensions(List<String> extensions) {
        extensionsValue().setValue(extensions);
        return this;
    }    
       

}
