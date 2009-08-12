package ch.deif.meander.map;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;

public class ComputeIndexTask extends TaskValue<LatentSemanticIndex> {

    public ComputeIndexTask(Value<Collection<String>> elements) {
        super("Creating latent semantic index", elements);
    }

    @Override
    protected LatentSemanticIndex computeValue(ProgressMonitor monitor, Arguments args) {
        Collection<String> elements = args.next();
        monitor.begin(elements.size());
        CorpusBuilder builder = Hapax.newCorpus()
                .ignoreCase()
                .useCamelCaseScanner()
                .rejectRareTerms()
                .rejectStopwords()
                .latentDimensions(25)
                .useTFIDF();
        for (String path: elements) {
            parseElement(builder, path);
            monitor.worked(1);
        }
        return builder.makeTDM().createIndex();
    }

	protected void parseElement(CorpusBuilder builder, String path) {
		try {
			builder.addDocument(path, new FileInputStream(path));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}
    
}
