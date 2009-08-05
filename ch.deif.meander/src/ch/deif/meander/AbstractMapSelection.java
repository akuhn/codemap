package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractMapSelection implements Iterable<String> {
	
	public Iterable<Location> locationsOn(MapInstance map) {
		Collection<Location> result = new ArrayList<Location>();
		for(Location each: map.locations()) {
			if (!this.contains(each)) continue;
			result.add(each);
		}
		return result;
	}

	protected abstract boolean contains(Location each);

}
