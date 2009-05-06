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
	private Frame currentFrame;


	public ChunkOutput(File file ) throws FileNotFoundException {
		this.out = PositionableStreams.makeRandomAccessFile(file, "rw");
		this.stream = (PositionableStream) out;
		this.currentFrame = new Frame();
	}

	public ChunkOutput(String fname) throws FileNotFoundException {
		this(new File(fname));
	}

	public ChunkOutput() {
		ByteArrayOutputStream baos = PositionableStreams.makeByteArrayOutputStream();
		this.out = new DataOutputStream(baos);
		this.stream = (PositionableStream) baos;
		this.currentFrame = new Frame();
	}


	public final void beginChunk(int name) throws IOException {
		currentFrame.push(name);
	}

	public final void endChunk(int name) throws IOException {
		currentFrame.pop(name);
	}

	public final void write(int value) throws IOException {
		out.writeInt(value);
	}

	public final void write(float value) throws IOException {
		out.writeFloat(value);
	}

	public final void write(float[] array) throws IOException {
		out.writeInt(array.length);
		for (float each: array) out.writeFloat(each);
	}

	public final void write(float[] buffer, int offset, int length) throws IOException {
		for (int n = offset; n < length; n++) out.writeFloat(buffer[n]);
	}

	public final void write(int[] buffer, int offset, int length) throws IOException {
		for (int n = offset; n < length; n++) out.writeInt(buffer[n]);
	}
	
	public final void write(float[][] array) throws IOException {
		out.writeInt(array.length);
		for (float[] each: array) this.write(each);
	}

	public final void write(double value) throws IOException {
		out.writeDouble(value);
	}

	public final void write(long value) throws IOException {
		out.writeLong(value);
	}

	public final void writeUTF(String string) throws IOException {
		out.writeUTF(string);
		alignPosition();
	}

	public final void alignPosition() throws IOException {
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


	final class Frame {

		private final Frame parent;
		private final int mark;
		private final int name;

		public Frame() {
			this.parent = null;
			this.name = Chunks.NULL;
			this.mark = 0;
		}

		private Frame(Frame parent, int name, int mark) {
			this.parent = parent;
			this.name = name;
			this.mark = mark;
		}

		public final void push(int name) throws IOException {
			out.writeInt(name);
			out.writeInt(-1);
			currentFrame = new Frame(this, name, (int) stream.getPosition());
		}

		public final void pop(int name) throws IOException {
			assert this.name == name;
			int pos = (int) stream.getPosition();
			stream.seek(mark - 4);
			out.writeInt(pos - mark);
			stream.seek(pos);
			currentFrame = parent;
		}

	}


	public final <Kind> void writeChunk(Kind element) throws IOException {
		ChunkSpec spec = new ChunkSpec(element.getClass());
		beginChunk(spec.getName());
		spec.writeOn(element, this);
		endChunk(spec.getName());
	}
	
}