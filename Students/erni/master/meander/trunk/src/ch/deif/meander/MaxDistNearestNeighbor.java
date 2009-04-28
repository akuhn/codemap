package ch.deif.meander;

import java.awt.Point;


public class MaxDistNearestNeighbor extends NearestNeighbor {
	
	private double maxDist;
	
	public MaxDistNearestNeighbor(Map map, double maxDist) {
		super(map);
		this.maxDist = maxDist;
	}
	
	@Override
	public Point forLocation(Point point) {
		Point result = super.forLocation(point);
		return shortest <= maxDist ? result : null;
	}
	
}
