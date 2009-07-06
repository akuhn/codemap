package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MapSelection {

	private HashSet<String> identifiers;
	
	public MapSelection() {
		identifiers = new HashSet<String>();		
	}
	
	public Iterable<Location> locationsOn(MapInstance map) {
		Collection<Location> result = new ArrayList<Location>();
		for(Location each: map.locations()) {
			if (!this.contains(each)) continue;
			result.add(each);
		}
		return result;
	}

	public int size() {
		return identifiers.size();
	}

	public MapSelection clear() {
		identifiers.clear();
		return this;
	}
	
	public boolean contains(Location element) {
		return identifiers.contains(element.getIdentifier());
	}

	public void add(String identifier) {
		identifiers.add(identifier);
	}

	public void remove(String identifier) {
		identifiers.remove(identifier);
	}
	
}
