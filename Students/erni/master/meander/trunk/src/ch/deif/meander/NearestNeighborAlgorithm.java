package ch.deif.meander;

import ch.deif.meander.Map.Pixel;

/** Computes the nearest neighbor for each pixel
 *<P>
 * Very very naive implementation, needs optimization!
 * 
 * @author Adrian Kuhn
 *
 */
public class NearestNeighborAlgorithm extends MapAlgorithm {

	private Location[][] NN;

	public NearestNeighborAlgorithm(Map map) {
		super(map);
	}

	@Override
	public void run() {
		setup();
		compute();
		update(map);
	}

	private void update(Map map) {
		map.setNearestNeighbors(this.NN);
	}

	private void compute() {
		for (Pixel p: map.pixels()) {
			int nearestDist2 = Integer.MAX_VALUE;
			Location nearestLocation = null;
			for (Location each: map.locations()) {
				int dist2 = (p.px - each.px()) * 2 + (p.py - each.py()) * 2;
				if (dist2 < nearestDist2) {
					nearestDist2 = dist2;
					nearestLocation = each;
				}
			}
			NN[p.px][p.py] = nearestLocation;
		}
	}

	private void setup() {
		this.NN = new Location[map.getWidth()][map.getWidth()];
	}

}
