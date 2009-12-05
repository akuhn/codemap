package ch.deif.meander.map;

import org.codemap.util.MapScheme;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.DEMAlgorithm;

public class ComputeElevationModelTask extends TaskValue<DigitalElevationModel> {

    public ComputeElevationModelTask(Value<MapInstance> mapInstance, Value<MapScheme<Boolean>> hills) {
        super("Creating digital elevation model", mapInstance, hills);
    }

    @Override
    protected DigitalElevationModel computeValue(ProgressMonitor monitor, Arguments arguments) {
        MapInstance map = arguments.nextOrFail();
        MapScheme<Boolean> hills = arguments.nextOrFail();
        if (hills == null) hills = new MapScheme<Boolean>(true);
        return computeValue(monitor, map, hills);
    }

    private DigitalElevationModel computeValue(ProgressMonitor monitor, MapInstance map, MapScheme<Boolean> hills) {
        DEMAlgorithm algorithm = new DEMAlgorithm();
        algorithm.setMap(processMap(map));
        return new DigitalElevationModel(algorithm.call());
    }

    protected MapInstance processMap(MapInstance map) {
        return map;
    }
}
