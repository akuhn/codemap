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
	@SuppressWarnings("unused")
	private final PositionableStream stream;
	private Frame currentFrame;

	public ChunkInput(File file) throws FileNotFoundException {
		this.in = PositionableStreams.makeRandomAccessFile(file, "r");
		this.stream = (PositionableStream) in;
		this.currentFrame = new Frame((int) file.length());
	}

	public ChunkInput(byte[] buffer) {
		ByteArrayInputStream bais = PositionableStreams.makeByteArrayInputStream(buffer);
		this.in = new DataInputStream(bais);
		this.stream = (PositionableStream) bais;
		this.currentFrame = new Frame(buffer.length);
	}

	public ChunkInput(int[] buffer) {
		this(makeByteArray(buffer));
	}

	private static byte[] makeByteArray(int[] buffer) {
		ByteBuffer target = ByteBuffer.allocate(buffer.length * 4);
		target.asIntBuffer().put(IntBuffer.wrap(buffer));
		return target.array();
	}

	public final void beginChunk() throws IOException {
		currentFrame.push();
	}

	public final void endChunk(int name) throws IOException {
		assert currentFrame.name == name;
		currentFrame.pop();
	}

	public final boolean hasMore() {
		return currentFrame.hasMore();
	}

	public final float readFloat() throws IOException {
		currentFrame.increment(4);
		return in.readFloat();
	}

	public final double readDouble() throws IOException {
		currentFrame.increment(8);
		return in.readDouble();
	}

	public final int readInt() throws IOException {
		currentFrame.increment(4);
		return in.readInt();
	}

	public final long readLong() throws IOException {
		currentFrame.increment(8);
		return in.readLong();
	}

	public final String readUTF() throws IOException {
		throw new UnsupportedOperationException();
	}

	public final int getName() {
		return currentFrame.name;
	}

	private final class Frame {

		private final Frame parent;
		private final int name;
		private final int limit;
		public int position;

		public Frame(int limit) {
			this.parent = null;
			this.name = Chunks.NULL;
			this.limit = limit;
			this.position = 0;
		}

		public final boolean hasMore() {
			return position < limit;
		}

		private Frame(Frame parent, int name, int limit) {
			this.parent = parent;
			this.name = name;
			this.limit = limit;
			this.position = 0;
		}

		public final void push() throws IOException {
			int name = in.readInt();
			int limit = in.readInt();
			currentFrame = new Frame(this, name, limit);
		}

		public final void pop() throws IOException {
			in.skipBytes(limit - position);
			parent.position += limit + 8;
			currentFrame = parent;
		}

		public final void increment(int count) throws IndexOutOfBoundsException {
			int newPosition = position + count;
			if (newPosition > limit) throw new IndexOutOfBoundsException();
			position = newPosition;
		}
		
	}

	public final int[] readIntArray() throws IOException {
		currentFrame.increment(4);
		int count = in.readInt();
		return readIntArray(count);
	}

	public final int[] readIntArray(int count) throws IOException {
		currentFrame.increment(count * 4);
		int[] result = new int[count];
		for (int n = 0; n < result.length; n++) result[n] = in.readInt();
		return result;
	}

	public final float[] readFloatArray() throws IOException {
		currentFrame.increment(4);
		int count = in.readInt();
		return readFloatArray(count);
	}

	public final float[] readFloatArray(int count) throws IOException {
		currentFrame.increment(count * 4);
		float[] result = new float[count];
		for (int n = 0; n < result.length; n++) result[n] = in.readFloat();
		return result;
	}
	
}