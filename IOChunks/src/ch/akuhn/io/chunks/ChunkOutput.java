package ch.akuhn.io.chunks;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class ChunkOutput {

	private final DataOutput out;
	private final PositionableStream stream;
	private final ChunkOutput parent;
	private final int mark;
	private final int name;

	public ChunkOutput(File file ) throws FileNotFoundException {
		this.out = PositionableStreams.makeRandomAccessFile(file, "rw");
		this.stream = (PositionableStream) out;
		this.parent = null;
		this.name = 0;
		this.mark = 0;
	}
	
	public ChunkOutput(String fname) throws FileNotFoundException {
		this(new File(fname));
	}

	public ChunkOutput() {
		ByteArrayOutputStream baos = PositionableStreams.makeByteArrayOutputStream();
		this.out = new DataOutputStream(baos);
		this.stream = (PositionableStream) baos;
		this.parent = null;
		this.name = 0;
		this.mark = 0;
	}
	
	private ChunkOutput(ChunkOutput parent, int name) throws IOException {
		this.parent = parent;
		this.out = parent.out;
		this.stream = parent.stream;
		this.name = name;
		this.mark = (int) stream.getPosition() + 8;
		out.writeInt(name);
		out.writeInt(-1);
	}

	public final ChunkOutput beginChunk(int name) throws IOException {
		return new ChunkOutput(this, name);
	}

	public final ChunkOutput endChunk(int name) throws IOException {
		assert this.name == name;
		int pos = (int) stream.getPosition();
		stream.seek(mark - 4);
		out.writeInt(pos - mark);
		stream.seek(pos);
		return parent;
	}

	public final void write(int value) throws IOException {
		out.writeInt(value);
	}

	public final void write(float value) throws IOException {
		out.writeFloat(value);
	}

	public final void write(double value) throws IOException {
		out.writeDouble(value);
	}

	public final void write(long value) throws IOException {
		out.writeLong(value);
	}

	public final void write(String string) throws IOException {
		out.writeUTF(string);
		alignPosition();
	}

	private final void alignPosition() throws IOException {
		int pos = (int) stream.getPosition();
		int modulo = pos % 4;
		if (modulo == 0) return;
		for (int n = 0; n < modulo; n++) out.writeByte(0);
	}

	public final byte[] toByteArray() {
		assert stream instanceof ByteArrayOutputStream; 
		return ((ByteArrayOutputStream) stream).toByteArray();
	}
	
	public final int[] toIntArray() {
		IntBuffer source = ByteBuffer.wrap(toByteArray()).asIntBuffer();
		return IntBuffer.allocate(source.limit()).put(source).array();
	}
	
}
