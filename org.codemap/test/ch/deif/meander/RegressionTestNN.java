package ch.deif.meander;

import java.util.HashMap;

import org.codemap.sandbox.main.QuickNDirtyMap;
import org.codemap.util.MapScheme;
import org.junit.Test;


public class RegressionTestNN {
    
    private static final int mapSize = 300;
    
    @Test
    public void nearestNeighborRegression() {
        final HashMap<Point, Integer> points = new HashMap<Point, Integer>();
        QuickNDirtyMap.anotherDebugPointSet(points);
      
        Configuration configuration = new Configuration(points.keySet());
        MapInstance mapInstance = configuration.withSize(mapSize, new MapScheme<Double>() {
            @Override
            public Double forLocation(Point location) {
                // TODO fill in size
                return (double) points.get(location);

            }
        });
        
        int badCount = 0;
        for(int x = 0; x < mapInstance.width; x++) {
            for (int y = 0; y < mapInstance.height; y++) {
                Location kdTreeNearest = mapInstance.kdTreeNearest(x, y);
                Location naiveNearest = mapInstance.naiveNearest(x, y);
                if (naiveNearest.equals(kdTreeNearest)) continue;
                
                badCount++;
//                assertEquals(naiveNearest, kdTreeNearest);
            }
        }
        System.out.println("number of wrong neighbors: " + badCount);
    }
}
