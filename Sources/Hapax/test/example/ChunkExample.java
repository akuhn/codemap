package example;

import java.io.IOException;

import ch.akuhn.hapax.linalg.SparseVector;
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.akuhn.io.chunks.ChunkSpec;

public class ChunkExample {

	public static void main(String... args) throws IOException {
		
		ChunkSpec spec = new ChunkSpec(SparseVector.class);
		ChunkOutput out = new ChunkOutput();
		spec.writeOn(new SparseVector(new double[] { 0, 0, 1, 2, 0, 3 }), out);
		for (int each: out.toIntArray()) {
			System.out.println(each);
		}
		ChunkInput in = new ChunkInput(out.toIntArray());
		System.out.println(in.readChunk(SparseVector.class));
	}
	
}
