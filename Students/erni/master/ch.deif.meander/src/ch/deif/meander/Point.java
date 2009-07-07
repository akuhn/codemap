package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;

/** A point on the visualization pane, with associated document.
 *  Coordinates are logical coordinates.
 *  When normalized, coordinates are typically between 0 and 1 (except for outliers).
 *<P>
 * Instances of this class are immutable.
 *  
 * @author Adrian Kuhn
 *
 */
public class Point {

	public final double x, y;
	private final Document document;

	public Point(double x, double y, Document document) {
		this.x = x;
		this.y = y;
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public Point normalize(double minX, double minY, double width, double height) {
		return new Point((x - minX) / width, (y - minY) / height, document);
	}

	public class Neighbor implements Comparable<Neighbor> {

		private static final double LAZY = -1.0;
		
		public final Point point;
		private double dist2;
		
		public Neighbor(Point point) {
			this.point = point;
			this.dist2 = LAZY;
		}

		@Override
		public int compareTo(Neighbor other) {
			return (int) Math.signum(this.dist2() - other.dist2());
		}

		private double dist2() {
			return dist2 == LAZY ? dist2 = computeDist2() : dist2;
		}

		private double computeDist2() {
			double dx = (point.x - Point.this.x);
			double dy = (point.y - Point.this.y);
			return  dx * dx + dy * dy;
		}
		
		public double distance() {
			return Math.sqrt(dist2());
		}
		
	}
	
	public Neighbor nearestNeighbor(Configuration map) {
		Neighbor nearest = null;
		for (Point each: map.points()) {
			Neighbor neighbor = new Neighbor(each);
			if (nearest.compareTo(neighbor) > 0) nearest = neighbor;
		}
		return nearest;
	}
	
}
