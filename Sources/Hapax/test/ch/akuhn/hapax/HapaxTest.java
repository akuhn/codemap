package ch.akuhn.hapax;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.hapax.linalg.Matrix;
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.unibe.jexample.*;

@RunWith(JExample.class)
public class HapaxTest {

	@Test
	public Hapax sampleHapax() {
		return Hapax.legomenon()
			.makeCorpus(".", "java");
	}
	
	@Test
	@Given("#sampleHapax")
	public byte[] writeToChunk(Hapax sample) throws IOException {
		ChunkOutput chunk = new ChunkOutput();
		chunk.writeChunk(sample);
		System.out.println(chunk.toByteArray().length);
		return chunk.toByteArray();
	}

	@Test
	@Given("#writeToChunk")
	public Hapax readFromChunk(byte[] bytes) throws IOException {
		ChunkInput chunk = new ChunkInput(bytes);
		return chunk.readChunk(Hapax.class);
	}
	
	@Test
	@Given("#sampleHapax,#readFromChunk")
	public void compareSampleAndChunk(Hapax sample, Hapax chunk) {
		Corpus a = sample.corpora().iterator().next();
		Corpus b = chunk.corpora().iterator().next();
		assertEquals(a.documentSize(), b.documentSize());
		assertEquals(a.termSize(), b.termSize());
		Matrix ma = ((TermDocumentMatrix) a).matrix();
		Matrix mb = ((TermDocumentMatrix) b).matrix();
		assertEquals(ma.rowSize(), mb.rowSize());
		assertEquals(ma.columnSize(), mb.columnSize());
		for (int n = 0; n < ma.rowSize(); n++) {
			for (int m = 0; m < ma.columnSize(); m++) {
				assertEquals(ma.get(n, m), mb.get(n, m));
			}
		}
		assertEquals(ma.density(), mb.density(), 1e-14);
		assertEquals(a.terms().uniqueSize(), b.terms().uniqueSize());
		assertEquals(a.terms().size(), b.terms().size());
		assertEquals(true, a.terms().containsAll(b.terms()));
		assertEquals(true, b.terms().containsAll(a.terms()));
	}
	
}
