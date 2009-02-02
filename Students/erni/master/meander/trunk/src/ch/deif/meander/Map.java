package ch.deif.meander;

import java.util.Arrays;
import java.util.Collection;

import ch.akuhn.util.Providable;

public class Map {

    private double[][] DEM;
    private double[][] hillshade;
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
        return new Pixel(n, m);
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
                Pixel p = new Pixel(n++, m);
                return p;
            }
        };
    }
    
    public Iterable<Kernel> kernels() {
        return new Providable<Kernel>() {
            private int n, m;

            @Override
            public void initialize() {
                n = m = 0;
            }

            @Override
            public Kernel provide() {
                if (n >= width) {
                    n = 0;
                    m++;
                }
                if (m >= height) return done();
                Kernel k = new Kernel(n++, m);
                return k;
            }
        };
        
    }

    public class Kernel extends Pixel {
        
        public double a, b, c, d, e, f, g, h, i;
        
        public Kernel(int n, int m) {
            super(n, m);
            int top = (m == 0 ? 0 : m - 1);
            int left = (n == 0 ? 0 : n - 1);
            int bottom = (m == (height - 1) ? (height - 1) : m + 1);
            int right = (n == (width - 1) ? (width - 1) : n + 1);

            a = getDEM()[left][top];
            b = getDEM()[n][top];
            c = getDEM()[right][top];
            d = getDEM()[left][m];
            e = getDEM()[n][m];
            f = getDEM()[right][m];
            g = getDEM()[left][bottom];
            h = getDEM()[n][bottom];
            i = getDEM()[right][bottom];
        }
        
        public void setHillshade(double hillshade) {
            getHillshade()[n][m] = hillshade;
        }

    }

    private double[][] getHillshade() {
        return hillshade == null ? hillshade = new double[width][height] : hillshade;
    }
    
    
    public class Pixel {

        int n;
        int m;

        public Pixel(int n, int m) {
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
