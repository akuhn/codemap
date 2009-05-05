package ch.deif.meander;

import ch.akuhn.util.Providable;
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
	private final int height;
	private int width;
	public LocationList locations;
	private Parameters parameters;
	private Location[][] NN;

	public Map(Parameters parameters, LocationList locations) {
		this.parameters = parameters;
		this.locations = locations;
		width = parameters.width;
		height = parameters.height;
		locations.normalizePixelXY(width);
	}

	private float[][] getDEM() {
		return DEM;
	}

	private boolean[][] getContours() {
		return contours == null ? contours = new boolean[getWidth()][getHeight()] : contours;
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
			private final int height = getHeight();

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
			private final int height = getHeight();

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
				if (y >= getHeight()) return done();
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
			int yBottom = (y == (getHeight() - 1) ? (getHeight() - 1) : y + 1);
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
		return hillshade == null ? hillshade = new double[getWidth()][getHeight()] : hillshade;
	}

	public class Pixel {

		int px;
		int py;
		private float[][] DEM0;

		public Pixel(int px, int py) {
			assert px < getWidth() && py < getHeight() : px + "," + py;
			this.px = px;
			this.py = py;
			this.DEM0 = getDEM();
		}

		public double x() {
			return (double) px / (getWidth() - 1);
		}

		public double y() {
			return (double) py / (getHeight() - 1);
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
			return NN[px][py].color();
		}

	}

	public static MapBuilder builder() {
		return new MapBuilder();
	}

	public int pixelSize() {
		return getWidth() * getHeight();
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
		// return new SketchVisualization(map);
		return new HillshadeVisualization(map);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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

}
