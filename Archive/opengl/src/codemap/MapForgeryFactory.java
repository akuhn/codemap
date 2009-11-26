package codemap;

import java.util.HashMap;

import ch.deif.meander.Configuration;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.util.MapScheme;

public class MapForgeryFactory {
    
    public static float[][] make(int mapSize) {
        
        final HashMap<Point, Integer> points = new HashMap<Point, Integer>();
        double f = 0.8;
        points.put(new Point(.1, .1*f, "LCD"), 128);
        points.put(new Point(.1, .15*f, ""), 30);        
        points.put(new Point(.15, .15*f, ""), 75);
        points.put(new Point(.13, .2*f, ""), 25);        
        points.put(new Point(.5, .15*f, ""), 40);
        points.put(new Point(.25, .2*f, ""), 60);
        points.put(new Point(.20, .18*f, ""), 70);
        
        points.put(new Point(.8, .2*f, "OEL"), 100);
        points.put(new Point(.7, .28*f, "OLED"), 160);
        points.put(new Point(.6, .3*f, ""), 100);
        points.put(new Point(.55, .28*f, ""), 80);
        points.put(new Point(.5, .25*f, ""), 50);
        points.put(new Point(.5, .5*f, "LED"), 120);
        points.put(new Point(.54, .55*f, ""), 40);
        points.put(new Point(.48, .53*f, ""), 33);
        
        points.put(new Point(.6, .4*f, ""), 50);
        points.put(new Point(.65, .35*f, ""), 60);
        
        Configuration configuration = new Configuration(points.keySet());
        
        MapInstance mapInstance = configuration.withSize(mapSize, new MapScheme<Double>() {
            @Override
            public Double forLocation(Point location) {
                return (double) points.get(location);
            }
        });  
        
        DEMAlgorithm algorithm = new DEMAlgorithm();
        algorithm.setMap(mapInstance);
        float[][] DEM = algorithm.call();
        DigitalElevationModel elevationModel = new DigitalElevationModel(DEM);
        return elevationModel.asFloatArray();
    }    
}
