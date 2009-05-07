package ch.akuhn.hapax;

import java.io.File;
import java.io.IOException;

import ch.akuhn.hapax.corpus.CamelCaseScanner;
import ch.akuhn.hapax.corpus.Corpora;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.CorpusBuilder;
import ch.akuhn.hapax.corpus.LetterScanner;
import ch.akuhn.hapax.corpus.PorterStemmer;
import ch.akuhn.hapax.corpus.Scanner;
import ch.akuhn.hapax.corpus.Stemmer;
import ch.akuhn.hapax.corpus.WordScanner;
import ch.akuhn.hapax.index.GlobalWeighting;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.LocalWeighting;
import ch.akuhn.hapax.index.Ranking;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.akuhn.io.chunks.ReadFromChunk;
import ch.akuhn.io.chunks.WriteOnChunk;

public class Hapax {

	private Corpora corpora;
	private TermDocumentMatrix buffer;
	private LatentSemanticIndex index;
	
	private boolean ignoreCase;
	private Scanner scanner;
	private Stemmer stemmer;
	private GlobalWeighting globalWeighting;
	private LocalWeighting localWeighting;
	private boolean rejectStopwords;
	private boolean rejectRareWords;

	private Hapax() {
		this.index = null;
		this.corpora = new Corpora();
		this.buffer = null;
	}
	
	public static Hapax legomenon() {
		return new Hapax();
	}

	public Hapax addFolder(String folder, String... extensions) {
		new CorpusBuilder(buffer).importAllFiles(new File(folder), extensions);
		return this;
	}
	
	public Hapax createIndex() {
		this.index = buffer.rejectAndWeight().createIndex();
		return this;
	}
	public Hapax dontIgnoreCase() {
		ignoreCase = false;
		return this;
	}
	public Hapax dontUseStemming() {
		stemmer = null;
		return this;
	}
	
	public Hapax dontWeight() {
		setGlobalWeighting(GlobalWeighting.NULL);
		setLocalWeighting(LocalWeighting.TERM);
		return this;
	}

	public Ranking<Document> find(String query) {
		return index.rankDocumentsByQuery(query).top(10);
	}

	public Hapax ignoreCase() {
		ignoreCase = true;
		return this;
	}

	public Hapax makeCorpus() {
		closeCorpus();
		buffer = new TermDocumentMatrix();
		return this;
	}
	
	public Hapax closeCorpus() {
		if (buffer != null) {
			if (ignoreCase) buffer = buffer.toLowerCase();
			if (rejectRareWords) buffer = buffer.rejectHapaxes();
			if (rejectStopwords) buffer = buffer.rejectStopwords();
			if (stemmer != null) buffer = buffer.stem(stemmer);
			corpora.add(buffer);
		}
		buffer = null;
		return this;
	}

	public Hapax makeCorpus(String source, String extensions) {
		makeCorpus();
		addFiles(source, extensions);
		closeCorpus();
		return this;
	}

	private Hapax addFiles(String source, String extensions) {
		if (buffer == null) throw new IllegalStateException("Please #makeCorpus first.");
		new CorpusBuilder(buffer).importFrom(source, extensions);
		return this;
	}
	
	public Hapax rejectRareWords() {
		rejectRareWords = true;
		return this;
	}

	public Hapax dontRejectRareWords() {
		rejectRareWords = true;
		return this;
	}
	private Hapax setGlobalWeighting(GlobalWeighting weighting) {
		this.globalWeighting = weighting;
		return this;
	}
	
	private Hapax setLocalWeighting(LocalWeighting weighting) {
		this.localWeighting = weighting;
		return this;
	}
	
	public Hapax useCamelCaseScanner() {
		scanner = new CamelCaseScanner();
		return this;
	}
	
	public void useDefaults() {
		useCamelCaseScanner();
		useStemming();
		ignoreCase();
		useTFIDF();
		rejectRareWords();
		rejectStopwords();
		//truncationRank();
	}
	
//	private Hapax truncationRank(int rank) {
//		// TODO Auto-generated method stub
//		
//	}

	public Hapax rejectStopwords() {
		rejectStopwords = true;
		return this;
	}

	public Hapax dontRejectStopwords() {
		rejectStopwords = true;
		return this;
	}
	
	public Hapax useLetterScanner() {
		scanner = new LetterScanner();
		return this;
	}
	
	public Hapax useStemming() {
		stemmer = new PorterStemmer();
		return this;
	}
	
	private Hapax useTFIDF() {
		setGlobalWeighting(GlobalWeighting.IDF);
		setLocalWeighting(LocalWeighting.TERM);
		return this;
	}
	
	public Hapax useWordScanner() {
		scanner = new WordScanner();
		return this;	
	}
	
	@WriteOnChunk("HPAX")
	public void storeOn(ChunkOutput chunk) throws IOException {
		chunk.write(0x20090507); // version
		chunk.writeChunk(corpora);
	}
	
	@ReadFromChunk("HPAX")
	public Hapax(ChunkInput chunk) throws IOException {
		assert chunk.readInt() == 0x20090507;
		corpora = chunk.readChunk(Corpora.class);
	}

	public Corpora corpora() {
		return corpora;
	}
	
}
