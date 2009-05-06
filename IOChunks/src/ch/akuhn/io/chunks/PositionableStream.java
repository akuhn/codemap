package ch.akuhn.io.chunks;

import java.io.IOException;

public interface PositionableStream {

	public long getPosition() throws IOException;
	
	public void seek(long position) throws IOException;
	
}
