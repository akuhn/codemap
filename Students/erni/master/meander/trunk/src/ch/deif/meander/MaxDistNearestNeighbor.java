package ch.deif.meander;

import java.awt.Point;

public class MaxDistNearestNeighbor extends NearestNeighbor {

	private double maxDist;

	public MaxDistNearestNeighbor(Map map, double maxDist) {
		super(map);
		this.maxDist = maxDist;
	}

	@Override
	public MaxDistNearestNeighbor forLocation(Point point) {
		super.forLocation(point);
		if (shortest > maxDist) {
			result = null;
		}
		return this;
	}

}
