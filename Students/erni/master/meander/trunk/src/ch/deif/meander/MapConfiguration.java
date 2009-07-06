package ch.deif.meander;

import java.util.Arrays;
import java.util.Collection;

import ch.deif.meander.util.Collect;

/** A set of documents on the visualization pane, each document has logical coordinates.
 *<P>
 * Instances of this class are immutable.
 *  
 * @author Adrian Kuhn
 *
 */
public class MapConfiguration {

	private Point[] locations;
	
	private MapConfiguration(Point... locations) {
		this.locations = locations;
	}
	
	public MapConfiguration(MapConfiguration map) {
		this(Arrays.copyOf(map.locations, map.locations.length));
	}
	
	public MapConfiguration(Collection<? extends Point> locations) {
		this(locations.toArray(new Point[locations.size()]));
	}
	
	public Iterable<Point> locations() {
		return Arrays.asList(locations);
	}
	
	public MapConfiguration normalize() {
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		for (Point each: this.locations()) {
			minY = Math.min(minY, each.getY());
			maxY = Math.max(maxY, each.getY());
			minX = Math.min(minX, each.getX());
			maxX = Math.max(maxX, each.getX());
		}
		double width = maxX - minX;
		double height = maxY - minY;
		Collect<Point> query = Collect.fromArray(locations);
		for (Collect<Point> each: query) {
			each.value = each.value.normalize(minX, minY, width, height);
		}
		return new MapConfiguration(query.asArray());
	}

	public MapInstance withSize(int size) {
		return new MapInstance(this, size).normalizeElevation();
	}

	public Object makeClone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new Error("Java specification forces us to catch an exception,"
					+ " that by the very specification itself may never occur. "
					+ " Weclome to the nightmare lands of static typing!", ex);
		}
	}
	
}
  