package ch.deif.meander;



public class DEMAlgorithm extends MapAlgorithm {

	private static final int MAGIC_VALUE = 6*320; // TODO avoid magic number for diameter of dem hills

	private static final double THRESHOLD = 1.0;

	private float[][] DEM;

	private int radius;

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
		final int y0, x0, top, bottom, left, right;
		y0 = each.py();
		x0 = each.px();
		top = y0 > radius ? 1 - radius : 0 - y0;
		left = x0 > radius ? 1 - radius : 0 - x0;
		bottom = y0 + radius < DEM.length ? radius : DEM.length - y0; 
		right = x0 + radius < DEM.length ? radius : DEM.length - x0; 
		for (int y = top; y < bottom; y++) {
			int absy = Math.abs(y);
			for (int x = left; x < right; x++) {
					DEM[x+x0][y+y0] += offscreen
					[Math.max(absy,Math.abs(x))]
					[Math.min(absy,Math.abs(x))];
			}
		}
	}

	private float[][] computePie(final double step, Location each) {
		float[][] offscreen = new float[DEM.length][];
		double e = each.normElevation();
		double factor = -1.0 
				/ (e * e) 
				* (MAGIC_VALUE * MAGIC_VALUE)
				/ ((DEM.length * DEM.length))
				/ 2;
		loop: for (int n = 0, n2 = 0; n < offscreen.length; n2 += (++n)+n-1) {
			offscreen[n] = new float[n+1];
			//assert n2 == n*n;
			for (int m = 0, dist2 = n2; m <= n; dist2 += (++m)+m-1) {
				//assert dist2 == n*n + m*m;
				double elevation = e * Math.exp(factor * (double) dist2);
				if (elevation < THRESHOLD) {
					if (m == 0) { 
						radius = n; break loop; 
					}
					break;
				}
				offscreen[n][m] += (elevation - THRESHOLD);
			}
		}
		return offscreen;
	}

	private void setup() {
		DEM = new float[map.getWidth()][map.getHeight()];
	}

}
