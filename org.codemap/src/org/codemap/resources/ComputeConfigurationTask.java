package org.codemap.resources;

import org.codemap.util.JobValue;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.Value;
import ch.deif.meander.Configuration;
import ch.deif.meander.builder.Meander;

public class ComputeConfigurationTask extends JobValue<Configuration> {

    public Configuration previousConfiguration;
    
    public ComputeConfigurationTask(String name, Value<LatentSemanticIndex> index) {
        super("Compute configuration of " + name, index);
        this.previousConfiguration = new Configuration();
    }

    @Override
    protected Configuration computeValue(ProgressMonitor monitor, Arguments args) {
        LatentSemanticIndex index = args.next();
        return previousConfiguration = Meander.builder().addCorpus(index).makeMap(previousConfiguration);
    }

    public ComputeConfigurationTask initialConfiguration(Configuration configuration) {
        this.previousConfiguration = configuration;
        return this;
    }

    
}
