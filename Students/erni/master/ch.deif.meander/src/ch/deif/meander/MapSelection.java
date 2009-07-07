package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/** A set of identifier handels.
 * External classes can use the collection interface to add and remove handles. 
 * Internal classes use {@link #locationsOn(MapInstance)} to iterate over all selected locations of a given map instance. 
 * 
 * @author Adrian Kuhn
 * @author David Erni
 *
 */
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

	public void replaceWith(Collection<String> ids) {
		identifiers = new HashSet<String>(ids);
	}
	
}
