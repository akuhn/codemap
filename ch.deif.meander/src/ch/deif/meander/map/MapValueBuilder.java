package ch.deif.meander.map;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.values.ReferenceValue;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.Labeling;
import ch.deif.meander.MapInstance;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class MapValueBuilder {

    private Configuration initialConfiguration;

    public void setInitialConfiguration(Configuration initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
    }

    public Value<Integer> mapSize() {
        return new ReferenceValue<Integer>(512);
    }

    public Value<Image> backgroundValue(Value<DigitalElevationModel> elevationModel, Value<HillShading> shading, Value<MapScheme<MColor>> colors) {
        return new ComputeBackgroundTask(elevationModel, shading, colors);
    }

    public Value<Collection<String>> elements() {
        return new ReferenceValue<Collection<String>>();
    }

    public Value<MapScheme<Boolean>> hills() {
        return new ReferenceValue<MapScheme<Boolean>>();
    }

    public Value<MapScheme<MColor>> colors() {
        return new ReferenceValue<MapScheme<MColor>>();
    }

    public Value<MapScheme<String>> labels() {
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

    public Value<CodemapVisualization> visualizationValue(Value<MapInstance> mapInstance) {
        return new ComputeVisualizationTask(mapInstance);
    }

    public Value<DigitalElevationModel> elevationModelValue(Value<MapInstance> mapInstance, Value<MapScheme<Boolean>> hills) {
        return new ComputeElevationModelTask(mapInstance, hills);
    }

    public Value<HillShading> hillShadingValue(Value<DigitalElevationModel> elevationModel) {
        return new ComputeHillShadingTask(elevationModel);
    }

    public Value<Labeling> labelingValue(Value<MapInstance> mapInstance, Value<MapScheme<String>> labels) {
        return new ComputeLabelingTask(mapInstance, labels);
    }

}
