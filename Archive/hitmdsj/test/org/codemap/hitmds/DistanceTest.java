package org.codemap.hitmds;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {

	private static final double Epsilon = 1e-2;

	@Test
	public void testDist() {
		Distance d = new Distance.DIST();
		double[] x = new double[] { 1, 0, 0 };
		double[] y = new double[] { 0, 1, 0 };
		double[] a = new double[] { 1, 2, 6 };
		double[] b = new double[] { 1, 6, 3 };
		assertEquals(0.00, d.dist(x, x), Epsilon);
		assertEquals(0.00, d.dist(y, y), Epsilon);
		assertEquals(1.41, d.dist(x, y), Epsilon);
		assertEquals(5.00, d.dist(a, b), Epsilon);
	}
	
}
