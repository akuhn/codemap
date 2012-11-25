package org.codemap.hitmds;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RegressionTest {

	private static final long SEED = 1L;
	private static final int LINES = Fixture.NOF_LINES / 2;
	private static final double EPSILON = 1e-6;
	
	@Test
	public void testGenesEndo4824() {
		double[][] actual = new Hitmds2().seed(SEED).run(Fixture.genesEndo(LINES));
		double[][] expected = new Revision37().seed(SEED).run(Fixture.genesEndo(LINES));
		assertEquals(LINES, expected.length);
		assertEquals(LINES, actual.length);
		assertEquals(2, expected[0].length);
		assertEquals(2, actual[0].length);
		for (int line = 0; line < LINES; line++) {
			assertEquals("line: " + line, expected[line][0], actual[line][0], EPSILON);
			assertEquals("line: " + line, expected[line][1], actual[line][1], EPSILON);
		}
	}
	
}
