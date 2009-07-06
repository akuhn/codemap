package ch.deif.aNewMeander;


public class LocationWithSize extends Location {

	protected int px, py;
	protected double elevation;
	protected MapColor color;

	public LocationWithSize(Location location, double elevation, int px, int py, MapColor color) {
		super(location);
		this.elevation = elevation;
		this.px = px;
		this.py = py;
		this.color = color;
	}

	public LocationWithSize(LocationWithSize loc) {
		this(loc, loc.elevation, loc.px, loc.py, loc.color);
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
