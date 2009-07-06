package ch.deif.aNewMeander;


public class LocationWithSize extends Location {

	protected int px, py;
	protected double elevation;

	public LocationWithSize(Location location, double elevation, int px, int py) {
		super(location);
		this.elevation = elevation;
		this.px = px;
		this.py = py;
	}

	public LocationWithSize(LocationWithSize loc) {
		this(loc, loc.elevation, loc.px, loc.py);
	}

	public LocationWithSize withElevation(double elevation) {
		LocationWithSize clone = (LocationWithSize) this.makeClone();
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
