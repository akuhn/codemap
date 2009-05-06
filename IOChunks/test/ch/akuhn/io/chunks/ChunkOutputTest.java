package ch.akuhn.io.chunks;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ChunkOutputTest {

	private static final int HEAD = 7000;

	@Test
	public void testWriteInt() throws IOException {
		ChunkOutput output = new ChunkOutput();
		output.write(100);
		output.write(200);
		output.write(300);
		int[] data = output.toIntArray();
		assertEquals(3, data.length);
		assertEquals(100, data[0]);
		assertEquals(200, data[1]);
		assertEquals(300, data[2]);
	}

	@Test
	public void testWriteLong() throws IOException {
		ChunkOutput output = new ChunkOutput();
		output.write(100L);
		output.write(200L);
		output.write(300L);
		int[] data = output.toIntArray();
		assertEquals(6, data.length);
		assertEquals(100, data[1]);
		assertEquals(200, data[3]);
		assertEquals(300, data[5]);
	}

	@Test
	public void testWriteChunk() throws IOException {
		ChunkOutput output = new ChunkOutput();
		ChunkOutput chunk = output.beginChunk(HEAD);
		chunk.write(100);
		chunk.write(200);
		chunk.write(300);
		ChunkOutput parent = chunk.endChunk(HEAD);
		assertEquals(output, parent);
		assertNotSame(output, chunk);
		int[] data = output.toIntArray();
		assertEquals(5, data.length);
		assertEquals(HEAD, data[0]);
		assertEquals(3*4, data[1]);
		assertEquals(100, data[2]);
		assertEquals(200, data[3]);
		assertEquals(300, data[4]);
	}
	
	
}
