package ch.deif.meander;


public class DEMAlgorithm extends MapAlgorithm {

	private static final int MAGIC_VALUE = 2000; // TODO avoid magic number for diameter of dem hills

	private float[][] DEM;
	private float[][] offscreen;
	
	public DEMAlgorithm(Map map) {
		super(map);
	}

	@Override
	public void run() {
		setup();
		compute();
		update(map);
	}
	
	private void update(Map map) {
		map.updateDEM(DEM);
	}

	private void compute() {
		final double step = 1.0 / (map.getWidth() - 1);
		for (Location each: map.locations()) {
			computePie(step, each);
			addPieToElevationModel(each);
		}
	}

	private void addPieToElevationModel(Location each) {
		for (int x = each.px(), ox = 0; x < DEM.length; x++, ox++) {
			for (int y = each.py(), oy = 0; y < DEM.length; y++, oy++) {
				DEM[x][y] += offscreen[ox][oy];
			}
		}
		for (int x = each.px() - 1, ox = 1; x >= 0; x--, ox++) {
			for (int y = each.py(), oy = 0; y < DEM.length; y++, oy++) {
				DEM[x][y] += offscreen[ox][oy];
			}
		}
		for (int x = each.px(), ox = 0; x < DEM.length; x++, ox++) {
			for (int y = each.py() - 1, oy = 1; y >= 0; y--, oy++) {
				DEM[x][y] += offscreen[ox][oy];
			}
		}
		for (int x = each.px() - 1, ox = 1; x >= 0; x--, ox++) {
			for (int y = each.py() - 1, oy = 1; y >= 0; y--, oy++) {
				DEM[x][y] += offscreen[ox][oy];
			}
		}
		for (int x = each.px(), ox = 0; x < DEM.length; x++, ox++) {
			for (int y = each.py(), oy = 0; y < DEM.length; y++, oy++) {
				DEM[x][y] += offscreen[oy][ox];
			}
		}
		for (int x = each.px() - 1, ox = 1; x >= 0; x--, ox++) {
			for (int y = each.py(), oy = 0; y < DEM.length; y++, oy++) {
				DEM[x][y] += offscreen[oy][ox];
			}
		}
		for (int x = each.px(), ox = 0; x < DEM.length; x++, ox++) {
			for (int y = each.py() - 1, oy = 1; y >= 0; y--, oy++) {
				DEM[x][y] += offscreen[oy][ox];
			}
		}
		for (int x = each.px() - 1, ox = 1; x >= 0; x--, ox++) {
			for (int y = each.py() - 1, oy = 1; y >= 0; y--, oy++) {
				DEM[x][y] += offscreen[oy][ox];
			}
		}
	}

	private void computePie(final double step, Location each) {
		offscreen = new float[map.getWidth()][map.getWidth()];
		outer: for (int n = 0; ; n++) {
			double x0 = each.x();
			double x = x0;
			double y0 = each.y();
			double y = y0;
			// TODO why do these return float instead of int ? ^^
			int px = n;
			int py = n;
			x += step * n;
			y += step * n;
			boolean first = true;
			while (true) {
				double dist = Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0));
				dist = dist / each.normElevation() * MAGIC_VALUE;
				double elevation = each.normElevation() * Math.exp(-(dist * dist / 2));
				if (first && elevation < 1.0) break outer;
				first = false;
				offscreen[px][py++] += (elevation - 1.0);
				y += step;
				if (elevation < 1.0) break;
			}
		}
	}

	private void setup() {
		DEM = new float[map.getWidth()][map.getHeight()];
	}

}
