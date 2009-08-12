package ch.deif.meander.map;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.Labeling;
import ch.deif.meander.MapInstance;
import ch.deif.meander.util.MapScheme;

public class ComputeLabelingTask extends TaskValue<Labeling> {

    public ComputeLabelingTask(Value<MapInstance> mapInstance, Value<MapScheme<String>> labels) {
        super("Label layout", mapInstance, labels);
    }

    @Override
    protected Labeling computeValue(ProgressMonitor monitor, Arguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

}
