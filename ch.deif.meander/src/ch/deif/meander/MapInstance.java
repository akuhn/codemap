package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;
import ch.akuhn.util.Providable;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.MapCaches;
import ch.deif.meander.kdtree.KDException;
import ch.deif.meander.kdtree.KDTree;
import ch.deif.meander.kdtree.KeySizeException;
import ch.deif.meander.main.Log;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class MapInstance {

    private MapCaches caches = new MapCaches(this);
    private ConcurrentMap<MapSetting<?>, Object> settings = new ConcurrentHashMap<MapSetting<?>, Object>();
    private Collection<Location> locations;
    public final int width, height;
    private KDTree<Location> kdTree;

    private MapInstance(Collection<Location> locations, int size, KDTree<Location> tree) {
        kdTree = tree;
        this.locations = locations;
        this.width = this.height = size;
    }
    public MapInstance(Configuration map, int size, MapScheme<Double> elevation) {
        locations = makeLocationsWithSize(map, size, elevation);
        kdTree = makeKdTree();
        this.width = this.height = size;
    }

    private KDTree<Location> makeKdTree() {
        Set<Location> locs = new TreeSet<Location>();
        locs.addAll(locations);
        if (locs.isEmpty()) return null;
        KDTree<Location> tree = new KDTree<Location>(2);
        for(Location each: locs) {
            try {
                tree.insert(each.getPoint().asDoubleArray(), each);
            } catch(KDException e) {
                Location found = searchLocation(tree, each.getPoint().asDoubleArray());
                System.out.println("duplicate locaiton, toinsert: " + each + "\nfound: " + found);
                //                throw new RuntimeException(e);
            }
        }
        return tree;            
    }
    private Location searchLocation(KDTree<Location> tree, double[] ds) {
        try {
            return tree.search(ds);
        } catch (KeySizeException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWidth() { // TODO rename to getSize()
        return width;
    }

    public <V> V get(Class<? extends MapAlgorithm<V>> key) {
        return caches.get(key);
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
        return kdTreeNearest(px, py);

    }

    public Location kdTreeNearest(int px, int py) {
        return new KdTreeLookup(kdTree, width).getResult(px, py);
    }

    public Location naiveNearest(int px, int py) {
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

    public KDTree<Location> getKdTree() {
        return kdTree;
    }
}
