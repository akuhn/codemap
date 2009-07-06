package ch.deif.meander;

import java.util.HashSet;
import java.util.Iterator;

public class MapSelection implements Iterable<Location> {

	private HashSet<String> identifiers;
	private Map map;
	
	public MapSelection() {
		identifiers = new HashSet<String>();		
	}
	
	@Override
	public Iterator<Location> iterator() {
		return currentLocations().iterator();
	}

	private Iterable<Location> currentLocations() {
		HashSet<Location> currentLocations = new HashSet<Location>();
		for(Location each: map.locations()) {
			if (identifiers.contains(each.getIdentifier())) {
				currentLocations.add(each);
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
		identifiers.clear();
		return this;
	}
	
	public boolean contains(Location element) {
		return identifiers.contains(element.getIdentifier());
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
