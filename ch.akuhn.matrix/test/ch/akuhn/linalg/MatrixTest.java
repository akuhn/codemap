package ch.akuhn.linalg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixTest {

	@Test
	public void shouldMultiplyMatrixAndVector() {
		Vector x = Vector.from(7,8,9);
		Matrix A = Matrix.from(2,3,
				1,2,3,
				4,5,6);
		assertEquals(3, x.size());
		assertEquals(2, A.rowCount());
		assertEquals(3, A.columnCount());
		assertEquals(5, A.get(1,1), Double.MIN_VALUE);
		Vector y = A.mult(x);
		assertEquals(2, y.size());
		assertEquals(7*1+8*2+9*3, y.get(0), Double.MIN_VALUE);
		assertEquals(7*4+8*5+9*6, y.get(1), Double.MIN_VALUE);
	}
	
	@Test(expected=AssertionError.class)
	public void shouldFailWhenSizeDoesNotConform() {
		Matrix.dense(3,2).mult(Vector.dense(3));
	}
	
}
