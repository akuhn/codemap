package ch.deif.meander.map;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.IntegerValue;
import ch.akuhn.values.ReferenceValue;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.Labeling;
import ch.deif.meander.MapSelection;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class MapValueBuilder {

    private Configuration initialConfiguration;

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
        return new ReferenceValue<Collection<String>>();
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
        return new ComputeIndexTask(elements);
    }

    public Value<Configuration> configurationValue(Value<LatentSemanticIndex> index) {
        return new ComputeConfigurationTask(index)
                .initialConfiguration(initialConfiguration);
    }

    public Value<MapInstance> mapInstanceValue(Value<Integer> mapSize, Value<LatentSemanticIndex> index, Value<Configuration> configuration) {
        return new ComputeMapInstanceTask(mapSize, index, configuration);
    }

    public Value<DigitalElevationModel> elevationModelValue(Value<MapInstance> mapInstance, Value<MapScheme<Boolean>> hills) {
        return new ComputeElevationModelTask(mapInstance, hills);
    }

    public Value<HillShading> hillShadingValue(Value<MapInstance> mapInstance, 
            Value<DigitalElevationModel> elevationModel) {
        return new ComputeHillShadingTask(mapInstance, elevationModel);
    }

    public Value<Labeling> labelingValue(Value<MapInstance> mapInstance, Value<MapScheme<String>> labels) {
        return new ComputeLabelingTask(mapInstance, labels);
    }

    public MapSelection currentSelectionValue() {
        return new MapSelection();
    }

    public MapSelection openFilesSelectionValue() {
        return new MapSelection();
    }

    public MapSelection youAreHereSelection() {
        return new MapSelection();
    }

}
