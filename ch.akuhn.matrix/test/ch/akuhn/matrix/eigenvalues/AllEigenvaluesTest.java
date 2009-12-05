package ch.akuhn.matrix.eigenvalues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.matrix.Matrix;
import ch.akuhn.matrix.SymetricMatrix;
import ch.akuhn.matrix.eigenvalues.AllEigenvalues;
import ch.akuhn.matrix.eigenvalues.Eigenvalues;
import ch.akuhn.util.Out;

public class AllEigenvaluesTest {

	@Test
	public void shouldFindEigenvalues() {
		Matrix A = Matrix.from(3, 3,
				0, 1, -1,
				1, 1, 0,
				-1, 0, 1);
		Eigenvalues eigen = new AllEigenvalues(A).run();
		
		assertEquals(-1, eigen.value[0], 1e-6);
		assertEquals(1, eigen.value[1], 1e-6);
		assertEquals(2, eigen.value[2], 1e-6);

		assert A.mult(eigen.vector[0]).equals(eigen.vector[0].times(eigen.value[0]), 1e-6);
		assert A.mult(eigen.vector[1]).equals(eigen.vector[1].times(eigen.value[1]), 1e-6);
		assert A.mult(eigen.vector[2]).equals(eigen.vector[2].times(eigen.value[2]), 1e-6);
	}
	
	
	@Test
	public void shouldFindEigenvalues2() {
		Matrix A = makeMatrix(50);
		Eigenvalues eigen = new AllEigenvalues(A).largest(5).run();
		Out.puts(eigen.value);
	}
	
	private Matrix makeMatrix(int n) {
		Matrix A = Matrix.dense(n, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				A.put(i, j, 1.0 / (i+j+3));
			}
			
		}
		return A;
	}
	
}
