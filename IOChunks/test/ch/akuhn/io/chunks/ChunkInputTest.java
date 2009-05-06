package ch.akuhn.io.chunks;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ChunkInputTest {

	private static final int HEAD = 7000;
	private static final int NULL = 0;

	@Test
	public void testReadInt() throws IOException {
		int[] data = new int[] { 0, 1, 2, 3 };
		ChunkInput in = new ChunkInput(data);
		assertEquals(0, in.readInt());
		assertEquals(1, in.readInt());
		assertEquals(2, in.readInt());
		assertEquals(3, in.readInt());
	}

	@Test
	public void testReadLong() throws IOException {
		int[] data = new int[] { 0, 1, 2, 3 };
		ChunkInput in = new ChunkInput(data);
		assertEquals(1L, in.readLong());
		assertEquals(0x200000003L, in.readLong());
	}

	@Test
	public void testReadChunk() throws IOException {
		int[] data = new int[] { HEAD, 8, 1, 2, 3 };
		ChunkInput chunk = new ChunkInput(data);
		assertEquals(NULL, chunk.getName());
		chunk.beginChunk();
		assertEquals(HEAD, chunk.getName());
		assertEquals(1, chunk.readInt());
		assertEquals(2, chunk.readInt());
		assertEquals(false, chunk.hasMore());
		chunk.endChunk(HEAD);
		assertEquals(NULL, chunk.getName());
		assertEquals(true, chunk.hasMore());
		assertEquals(3, chunk.readInt());
		assertEquals(false, chunk.hasMore());
	}
	
	@Test
	public void testSkipChunk() throws IOException {
		int[] data = new int[] { HEAD, 8, 1, 2, 3 };
		ChunkInput chunk = new ChunkInput(data);
		assertEquals(NULL, chunk.getName());
		chunk.beginChunk();
		assertEquals(HEAD, chunk.getName());
		// don't read, just skip 
		assertEquals(true, chunk.hasMore());
		chunk.endChunk(HEAD);
		assertEquals(NULL, chunk.getName());
		assertEquals(true, chunk.hasMore());
		assertEquals(3, chunk.readInt());
		assertEquals(false, chunk.hasMore());
	}
	
}
