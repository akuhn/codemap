package org.codemap.hitmds;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RegressionTest {

	private static final long SEED = 20090605L;
	private static final int LINES = 200;
	private static final double EPSILON = 1e-6;
	
	@Test
	public void testGenesEndo4824() {
		double[][] expected = new Revision37().seed(SEED).run(Fixture.genesEndo(LINES));
		double[][] actual = new Hitmds2().seed(SEED).run(Fixture.genesEndo(LINES));
		assertEquals(LINES, expected.length);
		assertEquals(LINES, actual.length);
		assertEquals(2, expected[0].length);
		assertEquals(2, actual[0].length);
		for (int line = 0; line < LINES; line++) {
			assertEquals(expected[line][0], actual[line][0], EPSILON);
			assertEquals(expected[line][1], actual[line][1], EPSILON);
		}
	}
	
}
