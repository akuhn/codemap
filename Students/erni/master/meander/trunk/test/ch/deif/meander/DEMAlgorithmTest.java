package ch.deif.meander;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.deif.meander.Map.Pixel;

public class DEMAlgorithmTest {

	private static final double VOLUME_UNDER_HILL = 16000.0; // precision is 1e2

	@Test
	public void testSingleHill() {
		Map map = Map.builder()
				.location(0.5, 0.5, 1000)
				.size(100)
				.done();
		new DEMAlgorithm(map).run();
		double minElevation = minElevation(map);
		double maxElevation = maxElevation(map);
		double volume = elevationVolume(map);
		assertEquals(0.0, minElevation, 1e-14);
		assertEquals(100.0, maxElevation, 1.0);
		assertEquals(VOLUME_UNDER_HILL, volume, 1e3);
	}
	
	@Test
	public void testTwoEqualHills() {
		Map map = Map.builder()
				.location(0.5, 0.6, 1000)
				.location(0.5, 0.4, 1000)
				.size(100)
				.done();
		new DEMAlgorithm(map).run();
		double minElevation = minElevation(map);
		double maxElevation = maxElevation(map);
		double volume = elevationVolume(map);
		assertEquals(0.0, minElevation, 1e-14);
		assertEquals(100.0, maxElevation, 1.0);
		assertEquals(2 * VOLUME_UNDER_HILL, volume, 1e3);
	}

	@Test
	public void testLargeAndSmallHills() {
		Map map = Map.builder()
				.location(0.5, 0.6, 1000)
				.location(0.5, 0.4, 500)
				.size(100)
				.done();
		new DEMAlgorithm(map).run();
		double minElevation = minElevation(map);
		double maxElevation = maxElevation(map);
		double volume = elevationVolume(map);
		assertEquals(0.0, minElevation, 1e-14);
		assertEquals(100.0, maxElevation, 1.0);
		assertEquals(1.125 * VOLUME_UNDER_HILL, volume, 1e3);
	}
	
	@Test
	public void testClippedHill() {
		Map map = Map.builder()
				.location(0.5, 0.0, 1000)
				.size(100)
				.done();
		new DEMAlgorithm(map).run();
		double minElevation = minElevation(map);
		double maxElevation = maxElevation(map);
		double volume = elevationVolume(map);
		assertEquals(0.0, minElevation, 1e-14);
		assertEquals(100.0, maxElevation, 1.0);
		assertEquals(0.5 * VOLUME_UNDER_HILL, volume, 1e3);		
	}

	@Test
	public void testClippedHill2() {
		Map map = Map.builder()
				.location(0.0, 0.0, 1000)
				.location(1.0, 0.0, 1000)
				.location(1.0, 1.0, 1000)
				.location(0.0, 1.0, 1000)
				.size(100)
				.done();
		new DEMAlgorithm(map).run();
		double minElevation = minElevation(map);
		double maxElevation = maxElevation(map);
		double volume = elevationVolume(map);
		assertEquals(0.0, minElevation, 1e-14);
		assertEquals(100.0, maxElevation, 1.0);
		assertEquals(1.0 * VOLUME_UNDER_HILL, volume, 1e3);		
	}

	@Test
	public void testBeyondBoundsHill() {
		Map map = Map.builder()
				.location(0.0, 0.0, 1000)
				.size(100)
				.done();
		new DEMAlgorithm(map).run();
		double minElevation = minElevation(map);
		double maxElevation = maxElevation(map);
		double volume = elevationVolume(map);
		assertEquals(0.0, minElevation, 1e-14);
		assertEquals(100.0, maxElevation, 1.0);
		assertEquals(0.25 * VOLUME_UNDER_HILL, volume, 1e3);		
	}
	
	
	private double maxElevation(Map map) {
		double maxElevation = Double.MIN_VALUE;
		for (Pixel each: map.pixels()) maxElevation = Math.max(maxElevation, each.elevation());
		return maxElevation;
	}

	private double minElevation(Map map) {
		double minElevation = Double.MAX_VALUE;
		for (Pixel each: map.pixels()) minElevation = Math.min(minElevation, each.elevation());
		return minElevation;
	}
	
	private double elevationVolume(Map map) {
		double volume = 0;
		for (Pixel each: map.pixels()) volume += each.elevation();
		return volume;
	}
	
}
