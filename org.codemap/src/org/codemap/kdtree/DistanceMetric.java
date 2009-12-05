// Abstract distance metric class

package org.codemap.kdtree;

abstract class DistanceMetric {
    
    protected abstract double distance(double [] a, double [] b);
}
