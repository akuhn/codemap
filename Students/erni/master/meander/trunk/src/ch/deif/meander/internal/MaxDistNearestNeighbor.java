package ch.deif.meander.internal;

import java.awt.Point;

import ch.deif.meander.Map;

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
