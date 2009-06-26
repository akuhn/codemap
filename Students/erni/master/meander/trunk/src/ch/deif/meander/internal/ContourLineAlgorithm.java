package ch.deif.meander.internal;

import ch.deif.meander.Map;
import ch.deif.meander.MapAlgorithm;
import ch.deif.meander.Map.Kernel;

public class ContourLineAlgorithm extends MapAlgorithm {

	public ContourLineAlgorithm(Map map) {
		super(map);
	}

	@Override
	public void run() {
		int step = getMap().getParameters().contourLineStep;
		for (Kernel k: getMap().kernels()) {
			int top = (int) Math.floor(k.top / step);
			int left = (int) Math.floor(k.left / step);
			int here = (int) Math.floor(k.here / step);
			boolean coastline = here == 0 || left == 0 || top == 0;
			k.setContourLine((top != here || left != here) && !coastline);
		}
	}

}