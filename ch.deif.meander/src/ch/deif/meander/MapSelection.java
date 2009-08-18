package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import ch.akuhn.values.CollectionValue;
import ch.deif.meander.map.MapValues;

/** A set of identifier handles.
 * External classes can use the collection interface to add and remove handles. 
 * Internal classes use {@link #locationsOn(MapInstance)} to iterate over all selected locations of a given map instance. 
 * 
 * @author Adrian Kuhn
 * @author David Erni
 *
 */
public class MapSelection extends CollectionValue<String> {
    
    public boolean contains(Location element) {
        return contains(element.getDocument());
    }
    
    public void replaceAll(Collection<String> newLocations) {
        boolean retainChange = value.retainAll(newLocations);
        boolean addChange = value.addAll(newLocations);
        if (retainChange || addChange) this.changed();
    }
    
    public MapSelection() {
        super(new HashSet<String>());
    }
    
    public Iterable<Location> locationsOn(MapValues map) {
        MapInstance mapInstance = map.mapInstance.getValue();
        if (mapInstance == null) return Collections.emptyList();
        return locationsOn(mapInstance);
    }
    
    private Iterable<Location> locationsOn(MapInstance mapInstance) {
        Collection<Location> result = new ArrayList<Location>();
        for(Location each: mapInstance.locations()) {
            if (!this.contains(each)) continue;
            result.add(each);
        }
        return result;
    }
}
