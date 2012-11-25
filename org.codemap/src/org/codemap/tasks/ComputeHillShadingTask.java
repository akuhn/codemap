package org.codemap.tasks;

import org.codemap.DigitalElevationModel;
import org.codemap.HillShading;
import org.codemap.MapInstance;
import org.codemap.internal.ShadeAlgorithm;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;

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
        ShadeAlgorithm hsa = new ShadeAlgorithm();
        hsa.setMap(mapInstance);
        double[][] shading = hsa.call();
        
        return new HillShading(shading);
    }
    
}
