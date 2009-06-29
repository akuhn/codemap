package ch.deif.meander;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MapSelection implements Iterable<Location> {

	private Set<Location> elements;
	
	private HashSet<String> identifiers;
	private Map map;
	
	public MapSelection() {
		elements = new HashSet<Location>();
		identifiers = new HashSet<String>();		
	}
	
	@Override
	public Iterator<Location> iterator() {
		return currentLocations().iterator();
	}

	private HashSet<Location> currentLocations() {
		HashSet<Location> currentLocations = new HashSet<Location>();
		for(String identifier: identifiers){
			for(Location each: map.locations()) {
				if (each.document().getIdentifier().equals(identifier)) {
					currentLocations.add(each);
					break;
				}
			}			
		}
		return currentLocations;
	}

	public int size() {
		return identifiers.size();
	}
	
	public MapSelection add(Location... selection) {
//		for (Location each: selection) {
//			elements.add(each);
//		}
//		return this;
//		currentlty disabled
		throw null;
	}
	
	public MapSelection add(Iterable<Location> selection) {
//		for (Location each: selection) {
//			elements.add(each);
//		}
//		return this;
//		currentlty disabled
		throw null;		
	}

	public MapSelection clear() {
		elements.clear();
		identifiers.clear();
		return this;
	}
	
	public boolean contains(Location element) {
		return currentLocations().contains(elements);
	}

	public void add(String handleIdentifier) {
		identifiers.add(handleIdentifier);
	}

	public void remove(String handleIdentifier) {
		identifiers.remove(handleIdentifier);
	}
	
	public MapSelection registerMap(Map map) {
		if (this.map != null) return this;
		this.map = map;
		return this;
	}
	
}
