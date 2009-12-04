// Abstract distance metric class

package ch.deif.meander.kdtree;

abstract class DistanceMetric {
    
    protected abstract double distance(double [] a, double [] b);
}
