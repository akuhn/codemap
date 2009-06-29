package ch.deif.meander;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MapSelection implements Iterable<Location> {

	private Set<Location> elements;
	
	public MapSelection() {
		elements = new HashSet<Location>();
	}
	
	@Override
	public Iterator<Location> iterator() {
		return elements.iterator();
	}

	public int size() {
		return elements.size();
	}
	
	public MapSelection add(Location... selection) {
		for (Location each: selection) {
			elements.add(each);
		}
		return this;
	}
	
	public MapSelection add(Iterable<Location> selection) {
		for (Location each: selection) {
			elements.add(each);
		}
		return this;
	}

	public MapSelection clear() {
		elements.clear();
		return this;
	}
	
	public boolean contains(Location element) {
		return elements.contains(elements);
	}
	
}
