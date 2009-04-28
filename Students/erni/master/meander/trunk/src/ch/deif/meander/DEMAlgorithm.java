package ch.deif.meander;

import ch.deif.meander.Map.Pixel;


public class DEMAlgorithm extends MapAlgorithm {
	
	private static final int MAGIC_VALUE = 2000; // TODO avoid magic number for
	
	// diameter of DEM hills.
	
	public DEMAlgorithm(Map map) {
		super(map);
	}
	
	@Override
	public void run() {
		for (Location each: map.locations()) {
			for (Pixel p: map.pixels()) {
				double dist = dist(each.x(), each.y(), p.x(), p.y());
				dist = dist / each.normElevation() * MAGIC_VALUE;
				double elevation = each.normElevation() * Math.exp(-(dist * dist / 2));
				p.increaseElevation(elevation);
			}
		}
	}
	
	private final double dist(double d, double e, double f, double g) {
		return Math.sqrt((d - f) * (d - f) + (e - g) * (e - g));
	}
	
}
