package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;

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

	public Document getDocument() {
		return point.getDocument();
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

	public Location withElevation(double elevation) {
		Location clone = new Location(this);
		clone.elevation = elevation;
		return clone;
	}

	public double getElevation() {
		return elevation;
	}

	public String getIdentifier() {
		return getDocument().getIdentifier();
	}

}
