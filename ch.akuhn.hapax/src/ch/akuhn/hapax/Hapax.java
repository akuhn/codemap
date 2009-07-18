package ch.akuhn.hapax;

import ch.akuhn.hapax.corpus.TermScanner;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.Ranking;

/** Searchable index of a text corpus.
 * 
 * @author Adrian Kuhn, 2009.
 *
 */
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
	
	public Ranking<String> find(String content) {
		Terms query = new Terms();
		scanner.newInstance().client(query).onString(content).run();
		if (ignoreCase) query = query.toLowerCase();
        return latentIndex.rankDocumentsByQuery(query);
	}
	
	public synchronized void updateDocument(String doc, String contents) {
		Terms document = scanner.fromString(contents);
		if (ignoreCase) document = document.toLowerCase();
		latentIndex.updateDocument(doc, document);
	}
	
	public synchronized void removeDocument(String doc) {
		latentIndex.removeDocument(doc);
	}
	
}
