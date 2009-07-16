package ch.akuhn.hapax;

import static ch.akuhn.util.Interval.range;

import java.util.Arrays;

import ch.akuhn.hapax.corpus.TermScanner;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.Bag.Count;



public final class Hapax {

    private TermScanner scanner;
	private boolean ignoreCase;
	private LatentSemanticIndex latentIndex;

	public Hapax(CorpusBuilder corpusBuilder) {
		this.scanner = corpusBuilder.scanner;
		this.ignoreCase = corpusBuilder.ignoreCase;
		this.latentIndex = corpusBuilder.makeTDM().createIndex(corpusBuilder.latentDimensions);
	}

	public static CorpusBuilder newCorpus() {
		return new CorpusBuilder();

	}
	
	public Query makeQuery(String content) {
		Terms query = new Terms();
		scanner.newInstance().client(query).onString(content).run();
		if (ignoreCase) query = query.toLowerCase();
        double[] pseudoDocument = latentIndex.createPseudoDocument(query);
        return new Query(pseudoDocument, latentIndex);
	}
	
}
