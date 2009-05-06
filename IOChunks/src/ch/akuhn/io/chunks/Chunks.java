package ch.akuhn.io.chunks;

import java.nio.ByteBuffer;

public class Chunks {

	public static final int makeName(String mnemonic) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		for (int n = 0; n < buffer.limit(); n++) 
			buffer.put(characterOrSpace(mnemonic, n));
		buffer.rewind();
		return buffer.asIntBuffer().get();
	}

	private static byte characterOrSpace(String mnemonic, int n) {
		return (byte) (n < mnemonic.length() ? mnemonic.charAt(n) : ' ');
	}
	
}
