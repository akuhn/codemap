package ch.deif.meander;

import ch.deif.meander.Map.Pixel;

public class DEMAlgorithm extends MapAlgorithm {

    private static final int MAGIC_VALUE = 2000; // TODO avoid magic number for diameter of DEM hills.

    public DEMAlgorithm(Map map) {
        super(map);
    }

    @Override
    public void run() {
        for (Location each: map.locations()) {
            for (Pixel p: map.pixels()) {
                double dist = dist(each.x, each.y, p.xNormed(), p.yNormed());
                dist = dist / each.height * MAGIC_VALUE; 
                double elevation = each.height * Math.exp(-(dist*dist/2));
                p.increaseElevation(elevation);
            }
        }
    }

    private final double dist(double x, double y, double x2, double y2) {
        return Math.sqrt((x - x2)*(x - x2) + (y - y2)*(y - y2));
    }

}
