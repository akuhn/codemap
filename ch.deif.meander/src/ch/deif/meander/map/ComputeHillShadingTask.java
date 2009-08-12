package ch.deif.meander.map;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;

public class ComputeHillShadingTask extends TaskValue<HillShading> {

    public ComputeHillShadingTask(Value<DigitalElevationModel> elevationModel) {
        super("Computing digital elevation model (if only Adrian and Dave would find time for some OpenGL-fu...)", elevationModel);
    }

    @Override
    protected HillShading computeValue(ProgressMonitor monitor, Arguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

}
