package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.codemap.resources.MapValues;

import ch.akuhn.values.CollectionValue;

/** A set of identifier handles.
 * External classes can use the collection interface to add and remove handles. 
 * Internal classes use {@link #locationsOn(MapInstance)} to iterate over all selected locations of a given map instance. 
 * 
 * @author Adrian Kuhn
 * @author David Erni
 *
 */
public class MapSelection extends CollectionValue<String> {
    
    public synchronized boolean contains(Location element) {
        return contains(element.getDocument());
    }
    
    public synchronized void replaceAll(Collection<String> newLocations) {
        boolean retainChange = value.retainAll(newLocations);
        boolean addChange = value.addAll(newLocations);
        if (retainChange || addChange) this.changed();
//        changed();
    }
    
    public MapSelection() {
        super(new HashSet<String>());
    }
    
    public synchronized Iterable<Location> locationsOn(MapValues map) {
        MapInstance mapInstance = map.mapInstance.getValue();
        if (mapInstance == null) return Collections.emptyList();
        return locationsOn(mapInstance);
    }
    
    private synchronized Iterable<Location> locationsOn(MapInstance mapInstance) {
        Collection<Location> result = new ArrayList<Location>();
        for(Location each: mapInstance.locations()) {
            if (!this.contains(each)) continue;
            result.add(each);
        }
        return result;
    }
}
