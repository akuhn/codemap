package ch.deif.meander.map;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.MapInstance;
import ch.deif.meander.swt.CodemapVisualization;

public class ComputeVisualizationTask extends TaskValue<CodemapVisualization> {

    public ComputeVisualizationTask(Value<MapInstance> mapInstance) {
        super("Visualization setup", mapInstance);
    }

    @Override
    protected CodemapVisualization computeValue(ProgressMonitor monitor, Arguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

}
