package ch.deif.meander;

import java.util.Arrays;
import java.util.Collection;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;

/** A set of documents on the visualization pane.
 * Each document is placed at a logical coordinate.
 * When normalized, most coordinates are between 0 and 1.
 * Such a placement of the documents is the result the Multidimensional Scaling (MDS) algorithm.
 *<P>
 * Instances of this class are immutable.
 *  
 * @author Adrian Kuhn
 *
 */
public class Configuration {

	private final Point[] points;
	
	private Configuration(Point... locations) {
		this.points = locations;
	}
	
	public Configuration(Configuration map) {
		this(Arrays.copyOf(map.points, map.points.length));
	}
	
	public Configuration(Collection<? extends Point> locations) {
		this(locations.toArray(new Point[locations.size()]));
	}
	
	public Configuration normalize() {
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		for (Point each: this.points()) {
			minY = Math.min(minY, each.y);
			maxY = Math.max(maxY, each.y);
			minX = Math.min(minX, each.x);
			maxX = Math.max(maxX, each.x);
		}
		double width = maxX - minX;
		double height = maxY - minY;
		Collect<Point> query = Collect.fromArray(points);
		for (Each<Point> each: query) {
			each.yield = each.value.normalize(minX, minY, width, height);
		}
		return new Configuration(query.resultArray());
	}

	public MapInstance withSize(int size) {
		return new MapInstance(this, size).normalizeElevation();
	}

	public Iterable<Point> points() {
		return Arrays.asList(points);
	}

	public double size() {
		return points.length;
	}
	
}
  