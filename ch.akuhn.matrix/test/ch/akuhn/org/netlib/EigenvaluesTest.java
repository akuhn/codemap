package ch.akuhn.org.netlib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.linalg.Matrix;
import ch.akuhn.org.netlib.lapack.Eigenvalues;

public class EigenvaluesTest {

	@Test
	public void shouldFindEigenvalues() {
		Matrix A = Matrix.from(3, 3,
				0, 1, -1,
				1, 1, 0,
				-1, 0, 1);
		Eigenvalues eigen = Eigenvalues.of(A);
		eigen.run();
		
		assertEquals(-1, eigen.value[0], 1e-6);
		assertEquals(2, eigen.value[1], 1e-6);
		assertEquals(1, eigen.value[2], 1e-6);

		assert A.mult(eigen.vector[0]).equals(eigen.vector[0].times(eigen.value[0]), 1e-6);
		assert A.mult(eigen.vector[1]).equals(eigen.vector[1].times(eigen.value[1]), 1e-6);
		assert A.mult(eigen.vector[2]).equals(eigen.vector[2].times(eigen.value[2]), 1e-6);
	}
	
	
}
