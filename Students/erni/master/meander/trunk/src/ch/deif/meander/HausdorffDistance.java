package ch.deif.meander;

/**
 * 
 * @see Dubuisson, M.-P., Jain, A.K., "A modified Hausdorff distance for object matching," 
 * Proceedings of the 12th IAPR International Conference on Pattern Recognition, Oct 1994.
 *
 */
public class HausdorffDistance {

    public double d(Location a, Map B) {
        double min = Double.POSITIVE_INFINITY;
        for (Location b: B.locations) {
            double dist = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
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
            // TODO do we need Kahan summation here?
            sum += d(a, B);
        }
        return sum / A.locationSize();
    }
    
    public double D18(Map A, Map B) {
        return Math.max(d5(A, B), d5(B, A));
    }
    
    public double D22(Map A, Map B) {
        return Math.max(d6(A, B), d6(B, A));
    }
    
    public double distance(Map one, Map two) {
        return D22(one, two);
    }

}