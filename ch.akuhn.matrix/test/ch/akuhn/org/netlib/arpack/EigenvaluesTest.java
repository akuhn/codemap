package ch.akuhn.org.netlib.arpack;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.linalg.Matrix;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.Injection;
import ch.unibe.jexample.InjectionPolicy;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
@Injection(InjectionPolicy.NONE) // there aint not side-effects
public class EigenvaluesTest {

	private Eigenvalues eigen;
	private Matrix A;

	private Matrix randomSymetricMatrix(int n) {
		Matrix A = Matrix.dense(n, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				double value = Math.random() * 23;
				A.put(i, j, value);
				A.put(j, i, value);
			}
		}
		return A;
	}
	
	@Test
	public Eigenvalues shouldDecomposeRandomArray() {
		A = randomSymetricMatrix(100);
		eigen = Eigenvalues.of(A).largest(40);
		eigen.run();
		assert eigen.value.length == 40;
		assert eigen.vector.length == 40;
		return eigen;
	}
	
	@Given("shouldDecomposeRandomArray")
	public void shouldReturnSortedEigenvalues() {
		for (int i = 1; i < 40; i++) {
			assert eigen.value[i-1] <= eigen.value[i];
		}
	}
		
	@Given("shouldDecomposeRandomArray")
	public void shouldReturnEigenvectors() {
		for (int i = 1; i < 40; i++) {
			assert eigen.vector[i] != null;
			assert eigen.vector[i].size() == 100;
		}
	}
		
	@Given("shouldDecomposeRandomArray")
	public void matrixEigenvectorMultiplicationShouldEqualEigenvectorTimesEigenvalue() {
		for (int i = 0; i < 40; i++) {
			assert eigen.vector[i].times(eigen.value[i]).equals(
					A.mult(eigen.vector[i]), 1e-6);
		}		
	}
	
}
