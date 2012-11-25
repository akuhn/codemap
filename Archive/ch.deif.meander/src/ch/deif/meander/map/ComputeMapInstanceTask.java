package ch.deif.meander.map;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public class ComputeMapInstanceTask extends TaskValue<MapInstance> {

    public ComputeMapInstanceTask(Value<Integer> size, 
            Value<LatentSemanticIndex> index, 
            Value<Configuration> configuration) {
        super(makeName(size), size, index, configuration);
    }

    private static String makeName(Value<Integer> size) {
        return String.format("Creating map (%d x %d pixel)", size.getValue(), size.getValue());
    }

    @Override
    protected MapInstance computeValue(ProgressMonitor monitor, Arguments args) {
        int size = args.nextOrFail();
        final LatentSemanticIndex index = args.nextOrFail();
        Configuration configuration = args.nextOrFail();
        return configuration.withSize(size, new MapScheme<Double>() {
            @Override
            public Double forLocation(Point location) {
                return Math.sqrt(index.getDocumentLength(location.getDocument()));

            }
        });
    }
    
}
