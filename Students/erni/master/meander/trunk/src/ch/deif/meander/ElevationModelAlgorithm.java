package ch.deif.meander;

import ch.akuhn.util.Out;
import ch.deif.meander.Map.Pixel;

public class ElevationModelAlgorithm extends MapAlgorithm {

    public ElevationModelAlgorithm(Map map) {
        super(map);
    }

    @Override
    public void run() {
        for (Location each: map.locations()) {
            Out.puts(each.x, each.y);
            for (Pixel p: map.pixels()) {
                double dist = dist(each.x, each.y, p.x(), p.y());
                dist = dist / each.height * 666; 
                double elevation = each.height * Math.exp(-(dist*dist/2));
                p.add(elevation * 2.55);
            }
        }
    }

    private final double dist(double x, double y, double x2, double y2) {
        return Math.sqrt((x - x2)*(x - x2) + (y - y2)*(y - y2));
    }

}
