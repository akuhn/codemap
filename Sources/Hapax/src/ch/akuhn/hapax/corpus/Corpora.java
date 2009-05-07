package ch.akuhn.hapax.corpus;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.akuhn.io.chunks.ReadFromChunk;
import ch.akuhn.io.chunks.WriteOnChunk;

public class Corpora implements Iterable<Corpus>{

	private List<Corpus> corpora = new LinkedList<Corpus>();
	
	public Corpora() {
		// do nothing
	}
	
	@ReadFromChunk("LIST")
	public Corpora(ChunkInput chunk) throws IOException {
		int count = chunk.readInt();
		for (int n = 0; n < count; n++) 
			corpora.add(chunk.readChunk(TermDocumentMatrix.class));
	}
	
	@WriteOnChunk("LIST")
	public void storeOn(ChunkOutput chunk) throws IOException {
		chunk.write(corpora.size());
		for (Corpus each: corpora) chunk.writeChunk(each);
	}

	public void add(Corpus corpus) {
		corpora.add(corpus);
	}

	public Iterator<Corpus> iterator() {
		return corpora.iterator();
	}
	
}
