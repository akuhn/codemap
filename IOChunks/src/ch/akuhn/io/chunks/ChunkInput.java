package ch.akuhn.io.chunks;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class ChunkInput {

	private final DataInput in;
	private final PositionableStream stream;
	private final ChunkInput parent;
	private final int name;
	private final int limit;
	private int count;
	
	public ChunkInput(File file) throws FileNotFoundException {
		this.in = PositionableStreams.makeRandomAccessFile(file, "r");
		this.stream = (PositionableStream) in;
		this.parent = null;
		this.name = 0;
		this.limit = (int) file.length();
	}
	
	public ChunkInput(byte[] buffer) {
		ByteArrayInputStream bais = PositionableStreams.makeByteArrayInputStream(buffer);
		this.in = new DataInputStream(bais);
		this.stream = (PositionableStream) bais;
		this.parent = null;
		this.name = 0;
		this.limit = buffer.length;
	}
	
	public ChunkInput(int[] buffer) {
		this(makeByteArray(buffer));
	}

	private static byte[] makeByteArray(int[] buffer) {
		ByteBuffer target = ByteBuffer.allocate(buffer.length * 4);
		target.asIntBuffer().put(IntBuffer.wrap(buffer));
		return target.array();
	}

	private ChunkInput(ChunkInput parent, int name) throws IOException {
		this.parent = parent;
		this.stream = parent.stream;
		this.in = parent.in;
		this.name = name;
		this.limit = in.readInt();
		this.count = 0;
	}

	public ChunkInput beginChunk() throws IOException {
		int name = in.readInt();
		return new ChunkInput(this, name);
	}

	public ChunkInput endChunk(int name) throws IOException {
		assert this.name == name;
		in.skipBytes(limit - count);
		if (parent != null) parent.count += limit + 8;
		return parent;
	}

	public final boolean hasMore() {
		return count < limit;
	}

	public final float readFloat() throws IOException {
		if (limit < count) throw new IndexOutOfBoundsException();
		count += 4;
		return in.readFloat();
	}

	public final double readDouble() throws IOException {
		if (limit < count) throw new IndexOutOfBoundsException();
		count += 8;
		return in.readDouble();
	}

	public final int readInt() throws IOException {
		if (limit < count) throw new IndexOutOfBoundsException();
		count += 4;
		return in.readInt();
	}

	public final long readLong() throws IOException {
		if (limit < count) throw new IndexOutOfBoundsException();
		count += 8;
		return in.readLong();
	}
	
	public final String readUTF() throws IOException {
		throw new UnsupportedOperationException();
	}

	public int getName() {
		return name;
	}


}
