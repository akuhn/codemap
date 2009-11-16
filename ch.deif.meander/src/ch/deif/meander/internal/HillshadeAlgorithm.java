package ch.deif.meander.internal;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import ch.akuhn.util.Pair;
import ch.deif.meander.MapAlgorithm;
import ch.deif.meander.MapSetting;
import ch.deif.meander.util.StopWatch;

/**
 * @see http://edndoc.esri.com/arcobjects/9.2/NET/shared/geoprocessing/ spatial_analyst_tools/how_hillshade_works.htm
 * @see Burrough, P. A. and McDonell, R.A., 1998. Principles of Geographical Information Systems (Oxford University
 *      Press, New York), p. 190.
 */
public class HillshadeAlgorithm extends MapAlgorithm<Pair<double[][], boolean[][]>> {

    private static final double Z_FACTOR = 0.6e-3;
    public static final MapSetting<Integer> CONTOUR_STEP = MapSetting.define("CONTOUR_STEP", 10);

    @Override
    public Pair<double[][], boolean[][]> call() {
        StopWatch stopWatch = new StopWatch("HillShade").start();
        
        // zenith: height of sun over horizon (90 = horizon, 0 = zenith).
        double zenithRad = 45 * PI / 180;
        // azimuth: direction of sun on x-y-plane
        double azimuthRad = (315 - 180) * PI / 180;
        double z_factor = Z_FACTOR * map.getWidth();
        
        int step = map.get(CONTOUR_STEP);
        
        double[][] hillshade = new double[map.width][map.width];
        float[][] DEM = map.getDEM();
        boolean[][] contour = new boolean[map.getWidth()][map.getWidth()];
        float topLeft, top, topRight, left, here, right, bottomLeft, bottom, bottomRight;        
        
        for (int x = 1; x < DEM.length-2; x++) {
            for (int y = 1; y < DEM[0].length-2; y++) {
                if ((here = DEM[x][y]) == 0.0) continue;
                // build kernel
                int yTop = y - 1;
                int xLeft = x - 1;
                int yBottom = y + 1;
                int xRight = x + 1;

                topLeft = DEM[xLeft][yTop];
                top = DEM[x][yTop];
                topRight = DEM[xRight][yTop];
                left = DEM[xLeft][y];
                right = DEM[xRight][y];
                bottomLeft = DEM[xLeft][yBottom];
                bottom = DEM[x][yBottom];
                bottomRight = DEM[xRight][yBottom];
                
                // calculate hillshading
                double dx = (topRight + (2 * right) + bottomRight - (topLeft + (2 * left) + bottomLeft)) / 8;
                double dy = (bottomLeft + (2 * bottom) + bottomRight - (topLeft + (2 * top) + topRight)) / 8;
                double slopeRad = atan(z_factor * sqrt(dx * dx + (dy * dy)));
                double aspectRad = atan2(dy, -dx);
                double shading = (cos(zenithRad) * cos(slopeRad) + (sin(zenithRad) * sin(slopeRad) * cos(azimuthRad
                        - aspectRad)));
                hillshade[x][y] = shading;
                // calculate contourlines
                int topHeight = (int) Math.floor(top / step);
                int leftHeight = (int) Math.floor(left / step);
                int hereHeight = (int) Math.floor(here / step);
                boolean coastline = topHeight == 0 || leftHeight == 0 || hereHeight == 0;
                contour[x][y] = ((topHeight != hereHeight || leftHeight != hereHeight) && !coastline);                
            }
        }
        stopWatch.printStop();
        return new Pair<double[][], boolean[][]>(hillshade, contour);
    }

}
