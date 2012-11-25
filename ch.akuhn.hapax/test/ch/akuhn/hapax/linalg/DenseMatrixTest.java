package ch.akuhn.hapax.linalg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class DenseMatrixTest {

	private static final double EPSILON = 1e-9;

	@Test
	public void testRowColumnVector() {
		DenseMatrix m = new DenseMatrix(30, 40);
		assertEquals(30, m.rowCount());
		assertEquals(40, m.columnCount());
		Vector row = m.row(7);
		assertEquals(40, row.size());
		Vector column = m.column(23);
		assertEquals(30, column.size());
		row.put(23, 2009);
		assertEquals(2009, row.get(23), EPSILON);
		assertEquals(2009, column.get(7), EPSILON);
		assertEquals(2009, m.get(7,23), EPSILON);
	}

	@Test
	public void testRowColumnVectorIterable() {
		DenseMatrix m = new DenseMatrix(30, 40);
		assertEquals(30, m.rowCount());
		assertEquals(40, m.columnCount());
		m.rows().iterator().next().put(23, 2323);
		m.columns().iterator().next().put(7, 77);
		assertEquals(2323, m.get(0,23), EPSILON);
		assertEquals(77, m.get(7,0), EPSILON);
	}
	
	
}
