package ch.deif.aNewMeander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.akuhn.util.Providable;
import ch.deif.meander.util.RunLengthEncodedList;
import ch.deif.meander.util.SparseTrueBooleanList;

public class MapInstance {

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

	}
	
	public class Pixel {

		private float[][] DEM0;
		public int px;
		public int py;

		public Pixel(int px, int py) {
			assert px < width && py < width : px + "," + py;
			this.px = px;
			this.py = py;
			this.DEM0 = getDEM();
		}

		public double elevation() {
			return DEM0[px][py];
		}

		public boolean hasContourLine() {
			return getContours().get(px).get(py);
		}

		public double hillshade() {
			return getHillshade()[px][py];
		}

		public void increaseElevation(double elevation) {
			if (elevation < 0) return;
			DEM0[px][py] += elevation;
		}

		public MapColor nearestNeighborColor() {
			// TODO go over color scheme
			// return NN == null ? MapColor.HILLGREEN : NN.get(px).get(py).color();
			return MapColor.HILLGREEN;
		}

		public void normalizeElevation(double maxElevation) {
			DEM0[px][py] = (float) (100.0 * (DEM0[px][py] / maxElevation));
		}

		public double x() {
			return (double) px / (width - 1);
		}
		
		public double y() {
			return (double) py / (width - 1);
		}

	}
	private int beachContourLevel = 10;
	private int contourLineStep = 10;
	private List<SparseTrueBooleanList> contours;
	private float[][] DEM;
	private boolean grayscale;
	private double[][] hillshade;
	private Collection<Location> locations;
	private List<RunLengthEncodedList<Point>> NN;
	private int waterContourLevel = 2;
	public final int width, height;

	public MapInstance(MapConfiguration map, int size) {
		locations = makeLocationsWithSize(map, size);
		this.width = this.height = size;
	}
	public Pixel get(int x, int y) {
		return new Pixel(x, y);
	}
	public int getBeachContourLevel() {
		return beachContourLevel;
	}
	public int getContourLineStep() {
		return contourLineStep;
	}
	private List<SparseTrueBooleanList> getContours() {
		if (contours == null) throw null;
		return contours;
	}
	public float[][] getDEM() {
		return DEM;
	}

	private double[][] getHillshade() {
		return hillshade == null ? hillshade = new double[getWidth()][getWidth()] : hillshade;
	}
	
	public int getWaterContourLevel() {
		return waterContourLevel;
	}
	
	public int getWidth() {
		return width;
	}

	public boolean isGrayscale() {
		return grayscale;
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

	public Iterable<Location> locations() {
		return locations;
	}

	private Collection<Location> makeLocationsWithSize(MapConfiguration map, int size) {
		Collection<Location> result = new ArrayList<Location>();
		for (Point each: map.locations()) {
			result.add(new Location(each, 
					Math.sqrt(each.getDocument().termSize()), // TODO do this in a mapper. maybe
					(int) (each.getX() * size),
					(int) (each.getY() * size)));
		}
		return result;
	}

	public void needElevationModel() {
		if (DEM == null) DEM = new DEMAlgorithm().runWith(this);
	}

	public void needHillshading() {
		if (hillshade == null) {
			hillshade = new HillshadeAlgorithm().runWith(this);
			contours = new ContourLineAlgorithm().runWith(this);
		}
	}
	public MapInstance normalizeElevation() {
		double max = 0.0;
		return this;
	}

	public Iterable<Pixel> pixels() {
		return pixelsByRows();
	}


	public Iterable<Pixel> pixelsByColumns() {
		return new Providable<Pixel>() {
			private final int height = getWidth();
			private int n, m;
			private Pixel pixel = new Pixel(0, 0);
			private final int width = getWidth();

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

	public Iterable<Pixel> pixelsByRows() {
		return new Providable<Pixel>() {
			private final int height = getWidth();
			private Pixel pixel = new Pixel(0, 0);
			private final int width = getWidth();
			private int x, y;

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

	public void setBeachContourLevel(int beachContourLevel) {
		this.beachContourLevel = beachContourLevel;
	}

	public void setContourLines(boolean[][] contours) {
		this.contours = new ArrayList<SparseTrueBooleanList>(contours.length);
		for (int row = 0; row < contours.length; row++) {
			// TODO use a simple List of RLE list in worst case
			this.contours.add(new SparseTrueBooleanList(contours[row]));
		}
	}

	public void setContourLineStep(int contourLineStep) {
		this.contourLineStep = contourLineStep;
	}

	public void setGrayscale(boolean grayscale) {
		// TODO withGrayscale
		this.grayscale = grayscale;
	}	
	
	public void setWaterContourLevel(int waterContourLevel) {
		this.waterContourLevel = waterContourLevel;
	}
	
}
