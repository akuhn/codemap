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
			NN[p.px][p.py] = map.nearestNeighbor(p.px, p.py);
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
