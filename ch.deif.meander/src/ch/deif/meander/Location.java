package ch.deif.meander;


/** Location of a document on a map instance.
 * Coordinates are in pixels.
 * When normalized, elevation is between 0 and 100.
 *<P>
 * Instances of this class are immutable.
 *  
 * @author Adrian Kuhn
 *
 */
public class Location {

	public final int px, py;
	protected double elevation;
	private Point point;

	public String getDocument() {
		return point.getDocument();
	}

	/** Returns short name of location, assuming that its full name is a filename.
	 * 
	 */
	public String getName() {
		String path = getDocument();
		int begin = path.lastIndexOf('/');
		int end = path.indexOf('.', begin);
		return path.substring(begin + 1, end < 0 ? path.length() : end);
	}
	
	public Location(Point point, double elevation, int px, int py) {
		this.point = point;
		this.elevation = elevation;
		this.px = px;
		this.py = py;
	}
	
	public Location(Location loc) {
		this(loc.point, loc.elevation, loc.px, loc.py); 
	}

	public Location withElevation(double newElevation) {
		Location clone = new Location(this);
		clone.elevation = newElevation;
		return clone;
	}

	public double getElevation() {
		return elevation;
	}

	@Deprecated
	public String getIdentifier() {
		return getDocument();
	}

	public Point getPoint() {
		return point;
	}

}
