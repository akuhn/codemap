package ch.deif.meander;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MapSelection implements Iterable<Location> {

	private Set<Location> elements;
	private HashSet<String> pendingElements;
	
	public MapSelection() {
		elements = new HashSet<Location>();
		pendingElements = new HashSet<String>();
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
		pendingElements.clear();
		return this;
	}
	
	public boolean contains(Location element) {
		return elements.contains(elements);
	}

	public void add(String handleIdentifier) {
		pendingElements.add(handleIdentifier);
	}

	public MapSelection convertElements(Map map) {
		for(String identifier: pendingElements){
			for(Location each: map.locations()) {
				if (each.document().getIdentifier().equals(identifier)) {
					add(each);
					break;
				}
			}			
		}
		pendingElements.clear();
		return this;
	}
	
}
