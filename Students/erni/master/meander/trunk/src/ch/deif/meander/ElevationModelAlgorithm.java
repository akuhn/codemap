package ch.deif.meander;

public class ElevationModelAlgorithm extends MapAlgorithm {

    public ElevationModelAlgorithm(Map map) {
        super(map);
    }

    @Override
    public void run() {
        for (Location each: map.locations()) {
            for (Pixel p: map.pixels()) {
                double dist = dist(each.x, each.y, p.x(), p.y());
                dist = dist / 100 / 0.5; // TODO magic constant for max-height of locations!
                double elevation = each.height * Math.exp(-(dist*dist/2));
                p.add(elevation);
            }
        }
    }

    private final double dist(double x, double y, double x2, double y2) {
        return Math.sqrt(x * x2 + y * y2);
    }

}
