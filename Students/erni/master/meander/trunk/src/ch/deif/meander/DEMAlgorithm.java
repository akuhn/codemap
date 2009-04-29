package ch.deif.meander;


public class DEMAlgorithm extends MapAlgorithm {

	private static final int MAGIC_VALUE = 2000; // TODO avoid magic number for diameter of dem hills

	private static final double THRESHOLD = 1.0;

	private static final boolean PACMAN = true;

	private float[][] DEM;
	
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
			elevateHill(each, computePie(step, each));
		}
	}

	private void elevateHill(Location each, float[][] offscreen) {
		final int length = DEM.length;
		final int radius = offscreen.length;
		// draw north-eastern pie, row by row
		for (int y = each.py(), n = 0; y >= 0 && n < radius; y--, n++) {
			for (int x = each.px(), m = 0; x < length && m < n; x++, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		// draw north-western pie, row by row
		for (int y = each.py(), n = 0; y >= 0 && n < radius; y--, n++) {
			for (int x = each.px() - 1, m = 1; x >= 0 && m <= n; x--, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		// draw western-north pie, column by column
		for (int x = each.px(), n = 0; x >= 0 && n < radius; x--, n++) {
			for (int y = each.py() - 1, m = 1; y >= 0 && m < n; y--, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		// draw western-south pie, column by column
		for (int x = each.px(), n = 0; x >= 0 && n < radius; x--, n++) {
			for (int y = each.py(), m = 0; y < length && m <= n; y++, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		// draw south-western pie, row by row
		for (int y = each.py(), n = 0; y < length && n < radius; y++, n++) {
			for (int x = each.px(), m = 0; x >= 0 && m < n; x--, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		// draw south-eastern pie, row by row
		for (int y = each.py(), n = 0; y < length && n < radius; y++, n++) {
			for (int x = each.px() + 1, m = 1; x < length && m <= n; x++, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		if (PACMAN) return;
		// draw eastern-south pie, column by column
		for (int x = each.px(), n = 0; x < length && n < radius; x++, n++) {
			for (int y = each.py(), m = 0; y < length && m < n; y++, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
		// draw eastern-north pie, column by column
		for (int x = each.px(), n = 0; x < length && n < radius; x++, n++) {
			for (int y = each.py() - 1, m = 1; y >= 0 && m <= n; y--, m++) {
				DEM[x][y] += offscreen[n][m];
			}
		}
	}

	private float[][] computePie(final double step, Location each) {
		float[][] offscreen = makeTraingleArray();
		for (int py = 0; py < offscreen.length; py++) {
			double x0 = each.x();
			double x = x0;
			double y0 = each.y();
			double y = y0;
			y += step * py;
			for (int px = 0; px <= py; px++) {
				// TODO should work with integer distances, etc...
				double dist = Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0));
				dist = dist / each.normElevation() * MAGIC_VALUE;
				double elevation = each.normElevation() * Math.exp(-(dist * dist / 2));
				if (elevation < THRESHOLD) break;
				offscreen[py][px] += (elevation - THRESHOLD);
				x += step;
			}
		}
		return offscreen;
	}

	private float[][] makeTraingleArray() {
		float[][] result = new float[100][0];
		for (int n = 0; n < result.length; n++) result[n] = new float[n+1];
		return result;
	}

	private void setup() {
		DEM = new float[map.getWidth()][map.getHeight()];
	}

}
