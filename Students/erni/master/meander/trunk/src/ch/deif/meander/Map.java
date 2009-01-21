package ch.deif.meander;

import java.util.Arrays;
import java.util.Collection;

import ch.akuhn.util.Providable;

public class Map {

    private double[][] DEM;
    public final int width, height;
    public Collection<Location> locations;
    private Parameters parameters;
    
    public Map(Parameters parameters, Location... locations) {
        this(parameters, Arrays.asList(locations));
    }

    public Map(Parameters parameters, Collection<Location> locations) {
        this.parameters = parameters;
        this.locations = locations;
        width = parameters.width;
        height = parameters.height;
    }
    
    private double[][] getDEM() {
        return DEM == null ? DEM = new double[width][height] : DEM;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Iterable<Location> locations() {
        return locations;
    }
    
    public MapVisualization createVisualization() {
        return new SketchVisualization(this);
    }
    
    public Pixel get(int n, int m) {
        return new Pixel(this, n, m);
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
                if (n >= width) {
                    n = 0;
                    m++;
                }
                if (m >= height) return done();
                Pixel p = new Pixel(Map.this, n++, m);
                return p;
            }
        };
    }

    public class Pixel {

        int n;
        int m;

        public Pixel(Map map, int n, int m) {
            assert n < width && m < height : n + "," + m;
            this.n = n;
            this.m = m;
        }

        public double x() {
            return (double) n / (width - 1);
        }

        public double y() {
            return (double) m / (height - 1);
        }

        public void add(double elevation) {
            if (elevation < 0) return;
            getDEM()[n][m] += elevation;
        }

        public double elevation() {
            return getDEM()[n][m];
        }

    }

    public static MapBuilder builder() {
        return new MapBuilder();
    }
    
    public Object pixelSize() {
        return width * height;
    }

    public Object locationSize() {
        return locations.size();
    }

    public Object hasDEM() {
        return DEM != null;
    }

}
