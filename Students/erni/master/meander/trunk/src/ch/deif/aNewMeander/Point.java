package ch.deif.aNewMeander;

public class Point implements Cloneable {

	protected double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this(p.x, p.y);
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
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
	
	public Point withXY(double x, double y) {
		Point clone = (Point) this.makeClone();
		clone.x = x;
		clone.y = y;
		return clone;
	}
	
}
