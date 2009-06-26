package ch.deif.meander;

import ch.akuhn.util.Providable;
import ch.deif.meander.internal.Colors;
import ch.deif.meander.internal.ContourLineAlgorithm;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.HillshadeAlgorithm;
import ch.deif.meander.internal.NearestNeighborAlgorithm;
import ch.deif.meander.internal.NormalizeElevationAlgorithm;
import ch.deif.meander.viz.HillshadeVisualization;
import ch.deif.meander.viz.MapVisualization;
import ch.deif.meander.viz.SketchVisualization;

/**
 * Main data container of maps.
 * 
 * @author Adrian Kuhn
 * @author David Erni
 */
public class Map {

	private float[][] DEM;
	private double[][] hillshade;
	private boolean[][] contours;
	private LocationList locations;
	private Parameters parameters;
	private Location[][] NN;

	public Map(Parameters parameters, LocationList locations) {
		this.parameters = parameters;
		this.locations = locations;
		locations.normalizePixelXY(getWidth());
	}

	public float[][] getDEM() {
		return DEM;
	}

	private boolean[][] getContours() {
		return contours == null ? contours = new boolean[getWidth()][getWidth()] : contours;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public Iterable<Location> locations() {
		return locations;
	}

	public MapVisualization createVisualization() {
		return new SketchVisualization(this);
	}

	public Pixel get(int x, int y) {
		return new Pixel(x, y);
	}

	public Iterable<Pixel> pixels() {
		return pixelsByRows();
	}

	public Iterable<Pixel> pixelsByRows() {
		return new Providable<Pixel>() {
			private int x, y;
			private Pixel pixel = new Pixel(0, 0);
			private final int width = getWidth();
			private final int height = getWidth();

			@Override
			public void initialize() {
				x = y = 0;
			}

			@Override
			public Pixel provide() {
				if (x >= width) {
					x = 0;
					y++;
					if (y >= height) return done();
				}
				pixel.px = x++;
				pixel.py = y;
				return pixel;
			}
		};
	}

	public Iterable<Pixel> pixelsByColumns() {
		return new Providable<Pixel>() {
			private int n, m;
			private Pixel pixel = new Pixel(0, 0);
			private final int width = getWidth();
			private final int height = getWidth();

			@Override
			public void initialize() {
				n = m = 0;
			}

			@Override
			public Pixel provide() {
				if (m >= height) {
					m = 0;
					n++;
					if (n >= width) return done();
				}
				pixel.px = n;
				pixel.py = m++;
				return pixel;
			}
		};
	}

	public Iterable<Kernel> kernels() {
		return new Providable<Kernel>() {
			private int x, y;

			@Override
			public void initialize() {
				x = y = 0;
			}

			@Override
			public Kernel provide() {
				if (x >= getWidth()) {
					x = 0;
					y++;
				}
				if (y >= getWidth()) return done();
				Kernel k = new Kernel(x++, y);
				return k;
			}
		};
	}

	public class Kernel extends Pixel {

		public double topLeft, top, topRight, left, here, right, bottomLeft, bottom, bottomRight;

		public Kernel(int x, int y) {
			super(x, y);
			int yTop = (y == 0 ? 0 : y - 1);
			int xLeft = (x == 0 ? 0 : x - 1);
			int yBottom = (y == (getWidth() - 1) ? (getWidth() - 1) : y + 1);
			int xRight = (x == (getWidth() - 1) ? (getWidth() - 1) : x + 1);

			topLeft = getDEM()[xLeft][yTop];
			top = getDEM()[x][yTop];
			topRight = getDEM()[xRight][yTop];
			left = getDEM()[xLeft][y];
			here = getDEM()[x][y];
			right = getDEM()[xRight][y];
			bottomLeft = getDEM()[xLeft][yBottom];
			bottom = getDEM()[x][yBottom];
			bottomRight = getDEM()[xRight][yBottom];
		}

		public void setHillshade(double hillshade) {
			getHillshade()[px][py] = hillshade;
		}

	}

	private double[][] getHillshade() {
		return hillshade == null ? hillshade = new double[getWidth()][getWidth()] : hillshade;
	}

	public class Pixel {

		public int px;
		public int py;
		private float[][] DEM0;

		public Pixel(int px, int py) {
			assert px < getWidth() && py < getWidth() : px + "," + py;
			this.px = px;
			this.py = py;
			this.DEM0 = getDEM();
		}

		public double x() {
			return (double) px / (getWidth() - 1);
		}

		public double y() {
			return (double) py / (getWidth() - 1);
		}

		public void increaseElevation(double elevation) {
			if (elevation < 0) return;
			DEM0[px][py] += elevation;
		}

		public double elevation() {
			return DEM0[px][py];
		}

		public double hillshade() {
			return getHillshade()[px][py];
		}

		public boolean hasContourLine() {
			return getContours()[px][py];
		}

		public void setContourLine(boolean bool) {
			getContours()[px][py] = bool;
		}

		public void normalizeElevation(double maxElevation) {
			DEM0[px][py] = (float) (100.0 * (DEM0[px][py] / maxElevation));
		}
		
		public Colors nearestNeighborColor() {
			return NN == null ? Colors.HILLGREEN : NN[px][py].color();
		}

	}

	public static MapBuilder builder() {
		return new MapBuilder();
	}

	public int pixelSize() {
		return getWidth() * getWidth();
	}

	public int locationCount() {
		return locations.count();
	}

	public Object hasDEM() {
		return DEM != null;
	}

	public MapVisualization getDefauVisualization() {
		// TODO make all algorithms reentrant.
		Map map = this;
		new DEMAlgorithm(map).run();
		new NormalizeElevationAlgorithm(map).run();
		new HillshadeAlgorithm(map).run();
		new ContourLineAlgorithm(map).run();
		new NearestNeighborAlgorithm(map).run();
		// return new SketchVisualization(map);
		return new HillshadeVisualization(map);
	}

	public int getWidth() {
		return getParameters().width;
	}

	public Location locationAt(int index) {
		return locations.at(index);
	}

	public void updateDEM(float[][] DEM) {
		this.DEM = DEM;
	}

	public void setNearestNeighbors(Location[][] NN) {
		this.NN = NN;
	}

	public void needElevationModel() {
		if (DEM == null) new DEMAlgorithm(this).run();
	}

	public void needHillshading() {
		if (hillshade == null) {
			new HillshadeAlgorithm(this).run();
			new ContourLineAlgorithm(this).run();
		}
	}

}
