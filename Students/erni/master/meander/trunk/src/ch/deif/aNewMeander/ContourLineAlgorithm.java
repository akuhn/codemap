package ch.deif.aNewMeander;

import java.util.ArrayList;
import java.util.List;

import ch.deif.aNewMeander.MapConfigurationWithSize.Kernel;
import ch.deif.meander.util.SparseTrueBooleanList;

public class ContourLineAlgorithm implements MapAlgorithm<List<SparseTrueBooleanList>> {

	@Override
	public List<SparseTrueBooleanList> runWith(MapConfigurationWithSize map) {
		int step = map.getContourLineStep();
		boolean[][] contour = new boolean[map.getWidth()][map.getWidth()];
		for (Kernel k: map.kernels()) {
			int top = (int) Math.floor(k.top / step);
			int left = (int) Math.floor(k.left / step);
			int here = (int) Math.floor(k.here / step);
			boolean coastline = here == 0 || left == 0 || top == 0;
			contour[k.px][k.py] = ((top != here || left != here) && !coastline);
		}
		ArrayList<SparseTrueBooleanList> result = new ArrayList<SparseTrueBooleanList>(contour.length);
		for (int row = 0; row < contour.length; row++) {
			// TODO use a simple List of RLE list in worst case
			result.add(new SparseTrueBooleanList(contour[row]));
		}
		return result;
	}

}
