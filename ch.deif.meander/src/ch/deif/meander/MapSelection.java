package ch.deif.meander;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/** A set of identifier handles.
 * External classes can use the collection interface to add and remove handles. 
 * Internal classes use {@link #locationsOn(MapInstance)} to iterate over all selected locations of a given map instance. 
 * 
 * @author Adrian Kuhn
 * @author David Erni
 *
 */
public class MapSelection extends AbstractMapSelection {

	private HashSet<String> identifiers;
	
	public MapSelection() {
		identifiers = new HashSet<String>();		
	}

	public int size() {
		return identifiers.size();
	}

	public MapSelection clear() {
		identifiers.clear();
		return this;
	}
	
	public boolean contains(Location element) {
		return identifiers.contains(element.getDocument());
	}

	public void add(String identifier) {
		identifiers.add(identifier);
	}

	public void remove(String identifier) {
		identifiers.remove(identifier);
	}
	
	public void replaceWith(Collection<String> newLocations) {
		identifiers.clear();
		identifiers.addAll(newLocations);
	}	

	public void addAll(Collection<String> ids) {
		identifiers.addAll(ids);
	}

	public void removeAll(Collection<String> ids) {
		identifiers.removeAll(ids);
	}

	@Override
	public Iterator<String> iterator() {
		return identifiers.iterator();
	}
	
}
