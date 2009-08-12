package org.codemap.resources;

import org.codemap.util.JobValue;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public class ComputeMapInstanceTask extends JobValue<MapInstance> {

    public ComputeMapInstanceTask(String name, 
            Value<Integer> size, 
            Value<LatentSemanticIndex> index, 
            Value<Configuration> configuration) {
        super("Compute map instance of " + name, size, index, configuration);
    }

    @Override
    protected MapInstance computeValue(ProgressMonitor monitor, Arguments args) {
        int size = args.next();
        final LatentSemanticIndex index = args.next();
        Configuration configuration = args.next();
        return configuration.withSize(size, new MapScheme<Double>() {
            @Override
            public Double forLocation(Point location) {
                return Math.sqrt(index.getDocumentLength(location.getDocument()));

            }
        });
    }
    
}
