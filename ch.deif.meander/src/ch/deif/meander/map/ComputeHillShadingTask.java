package ch.deif.meander.map;

import java.util.ArrayList;

import ch.akuhn.util.Pair;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.HillshadeAlgorithm;
import ch.deif.meander.util.SparseTrueBooleanList;

public class ComputeHillShadingTask extends TaskValue<HillShading> {

    public ComputeHillShadingTask(Value<MapInstance> mapInstance, Value<DigitalElevationModel> elevationModel) {
        super("Computing hill shading", mapInstance, elevationModel);
    }

    @Override
    protected HillShading computeValue(ProgressMonitor monitor, Arguments arguments) {
        MapInstance mapInstance = arguments.nextOrFail();
        DigitalElevationModel elevationModel = arguments.nextOrFail();
        return computeValue(monitor, mapInstance, elevationModel);
    }

    private HillShading computeValue(ProgressMonitor monitor, MapInstance mapInstance, DigitalElevationModel elevationModel) {
        HillshadeAlgorithm hsa = new HillshadeAlgorithm();
        hsa.setMap(mapInstance);
//        ContourLineAlgorithm cla = new ContourLineAlgorithm();
//        cla.setMap(mapInstance);
//        List<SparseTrueBooleanList> call2 = cla.call();
        Pair<double[][], boolean[][]> call = hsa.call();
        boolean[][] contour = call.snd;
        ArrayList<SparseTrueBooleanList> result = new ArrayList<SparseTrueBooleanList>(contour.length);
        for (int row = 0; row < contour.length; row++) {
            // TODO use a simple List of RLE list in worst case
            result.add(new SparseTrueBooleanList(contour[row]));
        }        
        
        return new HillShading(call.fst, result);
    }
    
}
