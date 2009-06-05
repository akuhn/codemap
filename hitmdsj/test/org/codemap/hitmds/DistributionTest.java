package org.codemap.hitmds;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistributionTest {

	private static final int LINES = 100;
	private static final long SEED = 1234567L;
	private double[] values;
	
	private double mean() {
		double sum = 0.0;
		for (double each: values) sum += each;
		return sum / values.length;
	}
	
	private double deviation() {
		double mean = mean();
		double sum = 0.0;
		for (double each: values) sum += (each - mean) * (each - mean);
		return Math.sqrt(sum / values.length);
	}
	
	@Test
	public void vertical() {
		double[][] points = computePoints();
		values = new double[LINES];
		for (int line = 0; line < LINES; line++) {
			values[line] = points[line][1];
		}
		assertEquals(0.0, mean(), 1e-6);
		assertEquals(1.0, deviation(), 0.1);
	}

	private double[][] computePoints() {
		return new Hitmds2().seed(SEED).run(Fixture.genesEndo(LINES));
	}
	
	@Test
	public void horizontal() {
		double[][] points = computePoints();
		values = new double[LINES];
		for (int line = 0; line < LINES; line++) {
			values[line] = points[line][0];
		}
		assertEquals(0.0, mean(), 1e-6);
		assertEquals(1.0, deviation(), 0.1);
	}
	
	@Test
	public void diagonal() {
		double[][] points = computePoints();
		values = new double[LINES];
		for (int line = 0; line < LINES; line++) {
			double x = points[line][0];
			double y = points[line][1];
			values[line] = 0.5 * Math.sqrt((x - y) * (x -y));
		}
		assertEquals(0.0, mean(), 0.2);
		assertEquals(1.0, deviation(), 0.1);
	}
	
	
	
}
