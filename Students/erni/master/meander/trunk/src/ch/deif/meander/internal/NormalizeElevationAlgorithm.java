package ch.deif.meander.internal;

import ch.deif.meander.Map;
import ch.deif.meander.MapAlgorithm;
import ch.deif.meander.Map.Pixel;

public class NormalizeElevationAlgorithm extends MapAlgorithm {

	public NormalizeElevationAlgorithm(Map map) {
		super(map);
	}

	@Override
	public void run() {
		double maxElevation = 0;
		for (Pixel p: getMap().pixels()) {
			maxElevation = Math.max(maxElevation, p.elevation());
		}
		for (Pixel p: getMap().pixels()) {
			p.normalizeElevation(maxElevation);
		}
	}

}
