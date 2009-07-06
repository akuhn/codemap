package ch.deif.aNewMeander;

import ch.akuhn.hapax.corpus.Document;


public class Location {

	protected int px, py;
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

	public int getPx() {
		return px;
	}

	public int getPy() {
		return py;
	}

	public double getElevation() {
		return elevation;
	}

}
