package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ch.deif.meander.map.MapValues;

public abstract class AbstractMapSelection implements Iterable<String> {

    public Iterable<Location> locationsOn(MapValues map) {
        MapInstance mapInstance = map.mapInstance.value();
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

    protected abstract boolean contains(Location each);

}
