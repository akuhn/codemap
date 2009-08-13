package ch.deif.meander.map;

import java.util.List;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.ContourLineAlgorithm;
import ch.deif.meander.internal.HillshadeAlgorithm;
import ch.deif.meander.util.SparseTrueBooleanList;

public class ComputeHillShadingTask extends TaskValue<HillShading> {

    public ComputeHillShadingTask(Value<MapInstance> mapInstance, Value<DigitalElevationModel> elevationModel) {
        super("Computing hill shading", mapInstance, elevationModel);
    }

    @Override
    protected HillShading computeValue(ProgressMonitor monitor, Arguments arguments) {
        MapInstance mapInstance = arguments.next();
        DigitalElevationModel elevationModel = arguments.next();
        return computeValue(monitor, mapInstance, elevationModel);
    }

    private HillShading computeValue(ProgressMonitor monitor, MapInstance mapInstance, DigitalElevationModel elevationModel) {
        HillshadeAlgorithm hsa = new HillshadeAlgorithm();
        hsa.setMap(mapInstance);
        ContourLineAlgorithm cla = new ContourLineAlgorithm();
        cla.setMap(mapInstance);
        double[][] call = hsa.call();
        List<SparseTrueBooleanList> call2 = cla.call();
        return new HillShading(call, call2);
    }
    
}
