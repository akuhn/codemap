package ch.deif.meander;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;
import ch.deif.meander.util.MapScheme;

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
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1337;
	private static final int VERSION_1 = 0x20090830;

	
	private Point[] points;
	
	private Configuration(Point... locations) {
		this.points = locations;
	}
	
	public Configuration(Configuration map) {
		this(Arrays.copyOf(map.points, map.points.length));
	}

    private void readObject(ObjectInputStream in) throws Exception {
        int version = in.readInt();
        if (version != VERSION_1) throw new Error();
        points = (Point[]) in.readObject();
        if (!this.invariant()) throw new Error();
    }
    
    private boolean invariant() {
        return true; // TODO Auto-generated method stub
    }

    private void writeObject(ObjectOutputStream out) throws Exception {
        out.writeInt(VERSION_1);
        out.writeObject(points);
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
		Collect<Point> query = Collect.from(points);
		for (Each<Point> each: query) {
			each.yield = each.value.normalize(minX, minY, width, height);
		}
		return new Configuration(query.getResultArray());
	}

	public MapInstance withSize(int size, MapScheme<Double> elevation) {
		return new MapInstance(this, size, elevation).normalizeElevation();
	}

	public Iterable<Point> points() {
		return Arrays.asList(points);
	}

	public double size() {
		return points.length;
	}
	
}
  