package ch.deif.meander;

/**
 * @see Dubuisson, M.-P., Jain, A.K., "A modified Hausdorff distance for object matching," Proceedings of the 12th IAPR
 *      International Conference on Pattern Recognition, Oct 1994.
 */
public class HausdorffDistance {

	public double d(Location a, Map B) {
		double min = Double.POSITIVE_INFINITY;
		for (Location b: B.locations) {
			double dist = (a.x() - b.x()) * (a.x() - b.x()) + (a.y() - b.y()) * (a.y() - b.y());
			if (dist < min) min = dist;
		}
		return Math.sqrt(min);
	}

	public double d5(Map A, Map B) {
		double max = 0;
		for (Location a: A.locations()) {
			double dist = d(a, B);
			if (max < dist) max = dist;
		}
		return max;
	}

	public double d6(Map A, Map B) {
		double sum = 0;
		for (Location a: A.locations) {
			// WAS do we need Kahan summation here? -- nope, we dont need, err =
			// approx 1e-16
			sum += d(a, B);
		}
		return sum / A.locationCount();
	}

	public double kahan_d6(Map A, Map B) {
		double sum = 0;
		double c = 0; // compensation
		for (Location a: A.locations) {
			double y = d(a, B) - c;
			double t = sum + y;
			c = (t - sum) - y;
			sum = t;
		}
		return sum / A.locationCount();
	}

	public double D18(Map A, Map B) {
		return Math.max(d5(A, B), d5(B, A));
	}

	public double D22(Map A, Map B) {
		double ab = d6(A, B);
		double ba = d6(B, A);
		return Math.max(ab, ba);
	}

	public double D23(Map A, Map B) {
		double ab = d6(A, B);
		double ba = d6(B, A);
		return ab + ba / 2;
	}

	public double distance(Map one, Map two) {
		return D23(one, two);
	}

	public double kahan_D22(Map A, Map B) {
		return Math.max(kahan_d6(A, B), kahan_d6(B, A));
	}

}
