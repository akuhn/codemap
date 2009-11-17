package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;
import ch.akuhn.util.Providable;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.MapCaches;
import ch.deif.meander.util.KdTree;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class MapInstance {

    private MapCaches caches = new MapCaches(this);
    private ConcurrentMap<MapSetting<?>, Object> settings = new ConcurrentHashMap<MapSetting<?>, Object>();
    private Collection<Location> locations;
    public final int width, height;
    private KdTree kdTree;

    private MapInstance(Collection<Location> locations, int size, KdTree tree) {
        kdTree = tree;
        this.locations = locations;
        this.width = this.height = size;
    }
    public MapInstance(Configuration map, int size, MapScheme<Double> elevation) {
        locations = makeLocationsWithSize(map, size, elevation);
        kdTree = makeKdTree();
        this.width = this.height = size;
    }

    private KdTree makeKdTree() {
        ArrayList<Location> list = new ArrayList<Location>();
        list.addAll(locations);        
        return new KdTree(list);
    }
    public Pixel get(int x, int y) {
        return new Pixel(x, y);
    }

    public int getWidth() { // TODO rename to getSize()
        return width;
    }

    public <V> V get(Class<? extends MapAlgorithm<V>> key) {
        return caches.get(key);
    }

    /**
     * warning, SLOW as hell!
     * if you need performance get the DEM manually and use for loops.
     * 
     */
    public Iterable<Kernel> kernels() {
        return new Providable<Kernel>() {
            private int x, y;

            @Override
            public void initialize() {
                x = y = 0;
            }

            @Override
            public Kernel provide() {
                if (x >= getWidth()) {
                    x = 0;
                    y++;
                }
                if (y >= getWidth()) return done();
                Kernel k = new Kernel(x++, y);
                return k;
            }
        };
    }

    public Iterable<Location> locations() {
        return locations;
    }

    private Collection<Location> makeLocationsWithSize(Configuration map, int size, MapScheme<Double> elevation) {
        Collection<Location> result = new ArrayList<Location>();
        for (Point each: map.points()) {
            Double e = elevation.forLocation(each);
            if (e == null || Double.isNaN(e.doubleValue())) throw new Error();
            result.add(new Location(each, e,
                    (int) (each.x * size),
                    (int) (each.y * size)));
        }
        return result;
    }

    public MapInstance normalizeElevation() {
        double max = maxElevation();
        if (max <= 0.0) return this;
        Collect<Location> query = Collect.from(locations);
        for (Each<Location> each: query) {
            each.yield = each.value.withElevation(each.value.getElevation() / max * 100);
        }
        MapInstance result = new MapInstance(query.getResult(), width, kdTree);
        result.settings = new ConcurrentHashMap<MapSetting<?>, Object>(this.settings);
        return result;
    }

    public Iterable<Pixel> pixels() {
        return pixelsByRows();
    }
    public Iterable<Pixel> pixelsByColumns() {
        return new Providable<Pixel>() {
            private final int fheight = getWidth();
            private int n, m;
            private Pixel pixel = new Pixel(0, 0);
            private final int fwidth = getWidth();

            @Override
            public void initialize() {
                n = m = 0;
            }

            @Override
            public Pixel provide() {
                if (m >= fheight) {
                    m = 0;
                    n++;
                    if (n >= fwidth) return done();
                }
                pixel.px = n;
                pixel.py = m++;
                return pixel;
            }
        };
    }

    public Iterable<Pixel> pixelsByRows() {
        return new Providable<Pixel>() {
            private final int fheight = getWidth();
            private Pixel pixel = new Pixel(0, 0);
            private final int fwidth = getWidth();
            private int x, y;

            @Override
            public void initialize() {
                x = y = 0;
            }

            @Override
            public Pixel provide() {
                if (x >= fwidth) {
                    x = 0;
                    y++;
                    if (y >= fheight) return done();
                }
                pixel.px = x++;
                pixel.py = y;
                return pixel;
            }
        };
    }


    public class Kernel extends Pixel {

        public double topLeft, top, topRight, left, here, right, bottomLeft, bottom, bottomRight;

        public Kernel(int x, int y) {
            super(x, y);
            int yTop = (y == 0 ? 0 : y - 1);
            int xLeft = (x == 0 ? 0 : x - 1);
            int yBottom = (y == (getWidth() - 1) ? (getWidth() - 1) : y + 1);
            int xRight = (x == (getWidth() - 1) ? (getWidth() - 1) : x + 1);

            float[][] DEM = get(DEMAlgorithm.class);

            topLeft = DEM[xLeft][yTop];
            top = DEM[x][yTop];
            topRight = DEM[xRight][yTop];
            left = DEM[xLeft][y];
            here = DEM[x][y];
            right = DEM[xRight][y];
            bottomLeft = DEM[xLeft][yBottom];
            bottom = DEM[x][yBottom];
            bottomRight = DEM[xRight][yBottom];
        }

    }	

    public class Pixel {

        private float[][] DEM0;
        public int px;
        public int py;

        public Pixel(int px, int py) {
            assert px < width && py < width : px + "," + py;
            this.px = px;
            this.py = py;
            this.DEM0 = get(DEMAlgorithm.class);
        }

        public double elevation() {
            return DEM0[px][py];
        }

        public void increaseElevation(double elevation) {
            if (elevation < 0) return;
            DEM0[px][py] += elevation;
        }

        public MColor nearestNeighborColor() {
            // TODO go over color scheme
            // return NN == null ? MapColor.HILLGREEN : NN.get(px).get(py).color();
            return MColor.HILLGREEN;
        }

        public void normalizeElevation(double maxElevation) {
            DEM0[px][py] = (float) (100.0 * (DEM0[px][py] / maxElevation));
        }

        public double x() {
            return (double) px / (width - 1);
        }

        public double y() {
            return (double) py / (width - 1);
        }

    }

    @SuppressWarnings("unchecked")
    public <V> V get(MapSetting<V> setting) {
        settings.putIfAbsent(setting, setting.defaultValue);
        return (V) settings.get(setting);
    }

    public <V> void reset(MapSetting<V> setting) {
        settings.remove(setting);
    }

    public <V> void set(MapSetting<V> setting, V value) {
        settings.put(setting, value);
    }

    public Location nearestNeighbor(int px, int py) {
//        return kdTree.getNearestNeighbor(new int[]{px, py});
        int nearestDist2 = Integer.MAX_VALUE;
        Location nearestLocation = null;
        for (Location each : locations()) {
            int dx = each.px - px;
            int dy = each.py - py;
            int dist2 = dx * dx + dy * dy;
            if (dist2 < nearestDist2) {
                nearestDist2 = dist2;
                nearestLocation = each;
            }
        }
        return nearestLocation;
    }

    public double maxElevation() {
        double max = 0.0;
        for (Location each: locations()) {
            max = Math.max(max, each.getElevation());
        }
        return max;
    }
    public boolean containsPoint(int x, int y) {
        return 0 <= x && 0 <= y && x < width && y < height;
    }
    public boolean isEmpty() {
        return locations.isEmpty();
    }

    public float[][] getDEM() {
        return get(DEMAlgorithm.class);
    }

}
