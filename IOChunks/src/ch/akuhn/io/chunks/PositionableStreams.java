package ch.akuhn.io.chunks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PositionableStreams {

	public static final RandomAccessFile makeRandomAccessFile(File file, String mode) throws FileNotFoundException {
		class PositionableRandomAccessFile extends RandomAccessFile implements PositionableStream {
			public PositionableRandomAccessFile(File file, String mode) throws FileNotFoundException {
				super(file, mode);
			}
			@Override
			public long getPosition() throws IOException {
				return getFilePointer();
			}
		}
		return new PositionableRandomAccessFile(file, mode);
	}
	
	public static final ByteArrayInputStream makeByteArrayInputStream(byte[] buffer) {
		class PositionableByteArrayInputStream extends ByteArrayInputStream implements PositionableStream {
			public PositionableByteArrayInputStream(byte[] buffer) {
				super(buffer);
			}
			@Override
			public long getPosition() throws IOException {
				return pos;
			}
			@Override
			public void seek(long position) throws IOException {
				this.pos = (int) position;
			}
		}
		return new PositionableByteArrayInputStream(buffer);
	}

	public static final ByteArrayOutputStream makeByteArrayOutputStream() {
		class PositionableByteArrayOutputStream extends ByteArrayOutputStream implements PositionableStream {
			@Override
			public long getPosition() throws IOException {
				return count;
			}
			@Override
			public void seek(long position) throws IOException {
				this.count = (int) position;
			}
		}
		return new PositionableByteArrayOutputStream();
	}
	
}
