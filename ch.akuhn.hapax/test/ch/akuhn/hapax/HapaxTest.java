package ch.akuhn.hapax;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.hapax.linalg.Matrix;
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class HapaxTest {

	@Test
	public TermDocumentMatrix sampleHapax() {
		return Hapax.newCorpus()
			.addFiles(".", "java")
			.makeTDM();
	}
	
	@Test
	@Given("#sampleHapax")
	public byte[] writeToChunk(TermDocumentMatrix sample) throws IOException {
		ChunkOutput chunk = new ChunkOutput();
		chunk.writeChunk(sample);
		System.out.println(chunk.toByteArray().length);
		return chunk.toByteArray();
	}

	@Test
	@Given("#writeToChunk")
	public TermDocumentMatrix readFromChunk(byte[] bytes) throws IOException {
		ChunkInput chunk = new ChunkInput(bytes);
		return chunk.readChunk(TermDocumentMatrix.class);
	}
	
	@Test
	@Given("#sampleHapax,#readFromChunk")
	public void compareSampleAndChunk(TermDocumentMatrix sample, TermDocumentMatrix chunk) {
		Corpus a = sample;
		Corpus b = chunk;
		assertEquals(a.documentCount(), b.documentCount());
		assertEquals(a.termCount(), b.termCount());
		Matrix ma = ((TermDocumentMatrix) a).matrix();
		Matrix mb = ((TermDocumentMatrix) b).matrix();
		assertEquals(ma.rowCount(), mb.rowCount());
		assertEquals(ma.columnCount(), mb.columnCount());
		for (int n = 0; n < ma.rowCount(); n++) {
			for (int m = 0; m < ma.columnCount(); m++) {
				assertEquals(ma.get(n, m), mb.get(n, m), 1e9);
			}
		}
		assertEquals(ma.density(), mb.density(), 1e-14);
		assertEquals(a.terms().uniqueSize(), b.terms().uniqueSize());
		assertEquals(a.terms().size(), b.terms().size());
		assertEquals(true, a.terms().containsAll(b.terms()));
		assertEquals(true, b.terms().containsAll(a.terms()));
	}
	
}
