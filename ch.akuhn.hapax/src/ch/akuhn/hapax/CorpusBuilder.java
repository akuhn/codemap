package ch.akuhn.hapax;

import java.io.File;

import ch.akuhn.hapax.corpus.CamelCaseScanner;
import ch.akuhn.hapax.corpus.LetterScanner;
import ch.akuhn.hapax.corpus.TermScanner;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.corpus.WordScanner;
import ch.akuhn.hapax.index.GlobalWeighting;
import ch.akuhn.hapax.index.LocalWeighting;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Files;

public final class CorpusBuilder {

	TermDocumentMatrix corpus = new TermDocumentMatrix();
	TermScanner scanner = new WordScanner();
	private LocalWeighting local = LocalWeighting.NULL;
	private GlobalWeighting global = GlobalWeighting.NULL;
	private boolean rejectStopwords = true;
	private boolean rejectRareTerms = true;
	private boolean rejectCommonTerms = true;
	boolean ignoreCase = true;
	int latentDimensions;

	
	public CorpusBuilder addDocument(String doc, String contents) {
		corpus.putDocument(doc, scanner.fromString(contents));
		return this;
	}

	
	public CorpusBuilder addFiles(String folder, String... extensions) {
		for (File each : Files.find(folder, extensions)) {
			corpus.putDocument(each.getAbsolutePath(), scanner.fromFile(each));
		}
		return this;
	}

	
	public CorpusBuilder dontUseWeighting() {
		local = LocalWeighting.NULL;
		global = GlobalWeighting.NULL;
		return this;
	}

	
	public TermDocumentMatrix makeTDM() {
		TermDocumentMatrix tdm = corpus;
		if (ignoreCase) tdm = tdm.toLowerCase();
		if (rejectRareTerms) tdm = tdm.rejectHapaxes();
		if (rejectStopwords) tdm = tdm.rejectStopwords();
		// TODO if (rejectCommonTerms) tdm = tdm.rejectCommonTerms();
		return tdm.weight(local, global);
	}

	
	public CorpusBuilder rejectCommonTerms() {
		rejectCommonTerms = true;
		return this;
	}

	
	public CorpusBuilder rejectRareTerms() {
		rejectRareTerms = true;
		return this;
	}

	
	public CorpusBuilder rejectStopwords() {
		rejectStopwords = true;
		return this;
	}

	
	public CorpusBuilder useCamelCaseScanner() {
		scanner = new CamelCaseScanner();
		return this;
	}

	
	public CorpusBuilder useTFIDF() {
		local = LocalWeighting.TERM;
		global = GlobalWeighting.IDF;
		return this;
	}

	
	public CorpusBuilder useWordScanner() {
		scanner = new WordScanner();
		return this;
	}

	
	public CorpusBuilder useLetterScanner() {
		scanner = new LetterScanner();
		return this;
	}

	
	public CorpusBuilder beCaseSensitiv() {
		ignoreCase  = false;
		return this;
	}

	
	public CorpusBuilder ignoreCase() {
		ignoreCase = true;
		return this;
	}

	public CorpusBuilder latentDimensions(int rank) {
		latentDimensions = rank;
		return this;
	}


	public Hapax build() {
		return new Hapax(this);
	}

}
