package ch.akuhn.matrix.eigenvalues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.matrix.Matrix;

public class FewEigenvaluesTest {

	private FewEigenvalues eigen;
	private Matrix A;

	private Matrix randomSymetricMatrix(int n) {
		Matrix S = Matrix.dense(n, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				double value = Math.random() * 23;
				S.put(i, j, value);
				S.put(j, i, value);
			}
		}
		return S;
	}

	@Test
	public void shouldDecomposeRandomArray() {
		A = randomSymetricMatrix(100);
		eigen = FewEigenvalues.of(A).largest(40);
		eigen.run();
		assert eigen.value.length == 40;
		assert eigen.vector.length == 40;
	}

	@Test
	// @Given("shouldDecomposeRandomArray")
	public void shouldReturnSortedEigenvalues() {
		shouldDecomposeRandomArray();
		for (int i = 1; i < 40; i++) {
			assert eigen.value[i - 1] <= eigen.value[i];
			assert eigen.value[i] > 0; // should be likely :)
		}
	}

	@Test
	// @Given("shouldDecomposeRandomArray")
	public void shouldReturnEigenvectors() {
		shouldDecomposeRandomArray();
		for (int i = 1; i < 40; i++) {
			assert eigen.vector[i] != null;
			assert eigen.vector[i].size() == 100;
		}
	}

	@Test
	// @Given("shouldDecomposeRandomArray")
	public void matrixEigenvectorMultiplicationShouldEqualEigenvectorTimesEigenvalue() {
		shouldDecomposeRandomArray();
		for (int i = 0; i < 40; i++) {
			assert eigen.vector[i].times(eigen.value[i]).equals(A.mult(eigen.vector[i]), 1e-6);
		}
	}

	@Test
	public void shouldDecomposeSmallMatrix() {
		A = Matrix.from(3, 3, 0, 1, -1, 1, 1, 0, -1, 0, 1);
		eigen = FewEigenvalues.of(A).largest(2);
		eigen.run();

		assertEquals(1, eigen.value[0], 1e-6);
		assertEquals(2, eigen.value[1], 1e-6);

		assert A.mult(eigen.vector[0]).equals(eigen.vector[0].times(eigen.value[0]), 1e-6);
		assert A.mult(eigen.vector[1]).equals(eigen.vector[1].times(eigen.value[1]), 1e-6);
	}

}
