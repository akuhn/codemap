package ch.deif.meander;

import java.util.Iterator;

import ch.akuhn.util.Providable;

public class Map {

    @SuppressWarnings("unused") double[][] DEM;
    private MapDescription description;
    public final int width, height;

    public Map(MapDescription description) {
        this.description = description;
        width = getParameters().width;
        height = description.getParameters().height;
        DEM = new double[width][height];
    }

    public Parameters getParameters() {
        return description.getParameters();
    }

    public Iterable<Location> locations() {
        return description.locations();
    }

    public MapVisualization createVisualization() {
        return new SketchVisualization(this);
    }

    public Iterable<Pixel> pixels() {
        return new Providable<Pixel>() {
            private int n, m;
            @Override
            public void initialize() {
                n = m = 0;
            }
            @Override
            public Pixel provide() {
                if (n >= width) { n = 0; m++; }
                if (m >= height) return done();
                Pixel p = new Pixel(Map.this, n++, m);
                return p;
            }
        };
    }
    
}
