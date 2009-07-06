package ch.deif.meander.internal;

import ch.deif.meander.MapConfiguration;
import ch.deif.meander.Point;

/**
 * @see Dubuisson, M.-P., Jain, A.K., "A modified Hausdorff distance for object matching," Proceedings of the 12th IAPR
 *      International Conference on Pattern Recognition, Oct 1994.
 */
public class HausdorffDistance {

	public double d(Point a, MapConfiguration B) {
		double min = Double.POSITIVE_INFINITY;
		for (Point b: B.points()) {
			double dist = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
			if (dist < min) min = dist;
		}
		return Math.sqrt(min);
	}

	public double d5(MapConfiguration A, MapConfiguration B) {
		double max = 0;
		for (Point a: A.points()) {
			double dist = d(a, B);
			if (max < dist) max = dist;
		}
		return max;
	}

	public double d6(MapConfiguration A, MapConfiguration B) {
		double sum = 0;
		for (Point a: A.points()) {
			// WAS do we need Kahan summation here? -- nope, we dont need, err =
			// approx 1e-16
			sum += d(a, B);
		}
		return sum / A.size();
	}

	public double kahan_d6(MapConfiguration A, MapConfiguration B) {
		double sum = 0;
		double c = 0; // compensation
		for (Point a: A.points()) {
			double y = d(a, B) - c;
			double t = sum + y;
			c = (t - sum) - y;
			sum = t;
		}
		return sum / A.size();
	}

	public double D18(MapConfiguration A, MapConfiguration B) {
		return Math.max(d5(A, B), d5(B, A));
	}

	public double D22(MapConfiguration A, MapConfiguration B) {
		double ab = d6(A, B);
		double ba = d6(B, A);
		return Math.max(ab, ba);
	}

	public double D23(MapConfiguration A, MapConfiguration B) {
		double ab = d6(A, B);
		double ba = d6(B, A);
		return ab + ba / 2;
	}

	public double distance(MapConfiguration one, MapConfiguration two) {
		return D23(one, two);
	}

	public double kahan_D22(MapConfiguration A, MapConfiguration B) {
		return Math.max(kahan_d6(A, B), kahan_d6(B, A));
	}

}
