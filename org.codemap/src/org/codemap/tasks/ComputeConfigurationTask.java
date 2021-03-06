package org.codemap.tasks;

import org.codemap.Configuration;
import org.codemap.resources.MapBuilder;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;


public class ComputeConfigurationTask extends TaskValue<Configuration> {

    public Configuration previousConfiguration;
    
    public ComputeConfigurationTask(Value<LatentSemanticIndex> index) {
        super("Computing map layout...", index);
        this.previousConfiguration = new Configuration();
    }

    @Override
    protected Configuration computeValue(ProgressMonitor monitor, Arguments args) {
        LatentSemanticIndex index = args.nextOrFail();
        return previousConfiguration = new MapBuilder().addCorpus(index).makeMap(previousConfiguration);
    }

    public ComputeConfigurationTask initialConfiguration(Configuration configuration) {
        this.previousConfiguration = configuration;
        return this;
    }

    
}
