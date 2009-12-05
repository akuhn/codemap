package org.codemap.internal;

import org.codemap.Location;
import org.codemap.MapAlgorithm;
import org.codemap.util.StopWatch;


/** Creates the digital elevation model of a map. A digital elevation model (DEM) is a raster of z-ordinates for each pixel. 
 *<p> 
 * For each location on the map, a hill is added to the digital elevation model. The elevation of a hill at position
 * (x0,y0) with height (z) is defined as follows, f(x,y) = z * exp ^ (-0.5 * (dist * factor) ^ 2) where
 * dist = sqrt((x - x0) ^ 2 + (y - y0) ^ 2) and factor = k / z. The constant (k) is chosen such that a hill of height
 * 100.0 has a diameter of 41% of the map.
 *<p>
 * The algorithm has been optimized to run fast.
 *<p> 
 * Not thread-safe.
 *  
 * @author Adrian Kuhn
 *
 */
public class DEMAlgorithm extends MapAlgorithm<float[][]> {

    public static final int MAGIC_VALUE = 8*320; // TODO magic number!
    private static final double THRESHOLD = 1.0;
    private float[][] DEM;
    private int radius;

    @Override
    public float[][] call() {
        setup();
        compute();
        return DEM;
    }

    private void compute() {
        StopWatch stopWatch = new StopWatch("DEM").start();
        // TODO a map configuration on map should return locations on map
        for (Location each: map.getDEMLocations()) {
            elevateHill(each, computePie(each));
        }
        stopWatch.printStop();
    }

    private void elevateHill(Location each, float[][] pie) {
        final int y0, x0, top, bottom, left, right;
        y0 = each.py;
        x0 = each.px;
        top = y0 > radius ? 1 - radius : 0 - y0;
        left = x0 > radius ? 1 - radius : 0 - x0;
        bottom = y0 + radius < DEM.length ? radius : DEM.length - y0; 
        right = x0 + radius < DEM.length ? radius : DEM.length - x0; 
        for (int y = top; y < bottom; y++) {
            int absy = Math.abs(y);
            for (int x = left; x < right; x++) {
                DEM[x+x0][y+y0] += pie
                [Math.max(absy,Math.abs(x))]
                 [Math.min(absy,Math.abs(x))];
            }
        }
    }

    private float[][] computePie(Location each) {
        float[][] pie = new float[DEM.length][];
        double elevationFactor = each.getElevation();
        double distFactor2 = -1.0 
        / (elevationFactor * elevationFactor) 
        * (MAGIC_VALUE * MAGIC_VALUE)
        / ((DEM.length * DEM.length))
        / 2;
        radius = computePieLoop(pie, elevationFactor, distFactor2);
        return pie;
    }

    private int computePieLoop(float[][] pie, double elevationFactor, double distFactor2) {
        for (int n = 0, n2 = 0; n < pie.length; n2 += (++n)+n-1) {
            pie[n] = new float[n+1];
            for (int m = 0, dist2 = n2; m <= n; dist2 += (++m)+m-1) {
                double elevation = elevationFactor * Math.exp(distFactor2 * (double) dist2);
                if (elevation < THRESHOLD) {
                    if (m == 0) return n;
                    break;
                }
                pie[n][m] += elevation - THRESHOLD;
            }
        }
        throw new Error("Should not happen, nach Adam Riese.");
    }

    private void setup() {
        DEM = new float[map.getWidth()][map.getWidth()];
    }


}
