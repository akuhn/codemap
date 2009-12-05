package ch.akuhn.matrix.eigenvalues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.matrix.Matrix;

public class AllEigenvaluesTest {

	private static final double epsilon = 1e-9;

	@Test
	public void shouldFindEigenvalues() {
		Matrix A = Matrix.from(3, 3,
				0, 1, -1,
				1, 1, 0,
				-1, 0, 1);
		Eigenvalues eigen = new AllEigenvalues(A).run();
		
		assertEquals(-1, eigen.value[0], epsilon);
		assertEquals(1, eigen.value[1], epsilon);
		assertEquals(2, eigen.value[2], epsilon);

		assert A.mult(eigen.vector[0]).equals(eigen.vector[0].times(eigen.value[0]), epsilon);
		assert A.mult(eigen.vector[1]).equals(eigen.vector[1].times(eigen.value[1]), epsilon);
		assert A.mult(eigen.vector[2]).equals(eigen.vector[2].times(eigen.value[2]), epsilon);
	}
	
	@Test
	public void shouldReturnLargest() {
		Matrix A = Matrix.dense(10, 10);
		for (int n = 0; n < 10; n++) A.put(n, n, n);
		Eigenvalues eigen = new AllEigenvalues(A).largest(3).run();
		assertEquals(3, eigen.value.length);
		assertEquals(7, eigen.value[0], epsilon);
		assertEquals(8, eigen.value[1], epsilon);
		assertEquals(9, eigen.value[2], epsilon);
	}
	
}
