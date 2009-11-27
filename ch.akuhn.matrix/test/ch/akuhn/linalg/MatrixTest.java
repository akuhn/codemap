package ch.akuhn.linalg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixTest {

	private static final double epsilon = Double.MIN_VALUE;

	@Test
	public void shouldMultiplySparseMatrixAndVector() {
		Vector x = Vector.from(8,9,10,11,12);
		Matrix A = new SparseMatrix(new double[][] {
				{0,1,0,2,0},
				{3,0,4,0,5},
				{0,6,0,7,0}});
		assertEquals(5, x.size());
		assertEquals(3, A.rowCount());
		assertEquals(5, A.columnCount());
		assertEquals(2, A.get(0,3), epsilon);
		assertEquals(3, A.get(1,0), epsilon);
		Vector y = A.mult(x);
		assertEquals(3, y.size());
		assertEquals(1*9+2*11, y.get(0), epsilon);
		assertEquals(3*8+4*10+5*12, y.get(1), epsilon);
		assertEquals(6*9+11*7, y.get(2), epsilon);
	}

	@Test
	public void shouldMultiplyTransposedSparseMatrixAndVector() {
		Vector x = Vector.from(8,9,10);
		Matrix A = new SparseMatrix(new double[][] {
				{0,1,0,2,0},
				{3,0,4,0,5},
				{0,6,0,7,0}});
		assertEquals(3, x.size());
		assertEquals(3, A.rowCount());
		assertEquals(5, A.columnCount());
		assertEquals(2, A.get(0,3), epsilon);
		assertEquals(3, A.get(1,0), epsilon);
		Vector y = A.transposeMultiply(x);
		assertEquals(5, y.size());
		assertEquals(3*9, y.get(0), epsilon);
		assertEquals(1*8+6*10, y.get(1), epsilon);
		assertEquals(4*9, y.get(2), epsilon);
		assertEquals(2*8+7*10, y.get(3), epsilon);
		assertEquals(5*9, y.get(4), epsilon);
	}
	
	@Test
	public void shouldMultiplyMatrixAndVector() {
		Vector x = Vector.from(7,8,9);
		Matrix A = Matrix.from(2,3,
				1,2,3,
				4,5,6);
		assertEquals(3, x.size());
		assertEquals(2, A.rowCount());
		assertEquals(3, A.columnCount());
		assertEquals(5, A.get(1,1), epsilon);
		Vector y = A.mult(x);
		assertEquals(2, y.size());
		assertEquals(7*1+8*2+9*3, y.get(0), epsilon);
		assertEquals(7*4+8*5+9*6, y.get(1), epsilon);
	}

	@Test
	public void shouldMultiplyTransposedMatrixAndVector() {
		Vector x = Vector.from(7,8,9);
		Matrix A = Matrix.from(3,2,
				1,2,
				3,4,
				5,6);
		assertEquals(3, x.size());
		assertEquals(3, A.rowCount());
		assertEquals(2, A.columnCount());
		assertEquals(4, A.get(1,1), epsilon);
		Vector y = A.transposeMultiply(x);
		assertEquals(2, y.size());
		assertEquals(7*1+8*3+9*5, y.get(0), epsilon);
		assertEquals(7*2+8*4+9*6, y.get(1), epsilon);
	}
	
	
	@Test(expected=AssertionError.class)
	public void shouldFailWhenSizeDoesNotConform() {
		Matrix.dense(3,2).mult(Vector.dense(3));
	}
	
}
