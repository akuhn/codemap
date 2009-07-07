package ch.deif.meander.internal;

import java.util.ArrayList;

import ch.deif.meander.Location;
import ch.deif.meander.MapAlgorithm;
import ch.deif.meander.MapInstance.Pixel;
import ch.deif.meander.util.RunLengthEncodedList;

/**
 * Computes the nearest neighbor for each pixel
 *<P>
 * Very very naive implementation, needs optimization!
 * 
 * @author Adrian Kuhn
 * 
 */
public class NearestNeighborAlgorithm extends
		MapAlgorithm<ArrayList<RunLengthEncodedList<Location>>> {

	@Override
	public ArrayList<RunLengthEncodedList<Location>> call() {
		Location[][] NN = new Location[map.width][map.height];
		for (Pixel p : map.pixels()) {
			int nearestDist2 = Integer.MAX_VALUE;
			Location nearestLocation = null;
			for (Location each : map.locations()) {
				int dx = each.px - p.px;
				int dy = each.py - p.py;
				double dist2 = dx * dx + dy * dy;
				if (dist2 < nearestDist2) {
					nearestDist2 = (int) dist2;
					nearestLocation = each;
				}
			}
			NN[p.px][p.py] = nearestLocation;
		}
		ArrayList<RunLengthEncodedList<Location>> result =
				new ArrayList<RunLengthEncodedList<Location>>(NN.length);
		for (int row = 0; row < NN.length; row++) {
			// TODO use a simple List of RLE list in worst case
			result.add(new RunLengthEncodedList<Location>(NN[row]));
		}
		return result;
	}

}
