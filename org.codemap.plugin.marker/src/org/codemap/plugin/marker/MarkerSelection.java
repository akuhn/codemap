package org.codemap.plugin.marker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.deif.meander.AbstractMapSelection;
import ch.deif.meander.Location;

public class MarkerSelection extends AbstractMapSelection {
	
	private Map<String, Integer> identifiers;

	public MarkerSelection() {
		identifiers = new HashMap<String, Integer>();
	}

	@Override
	public Iterator<String> iterator() {
		return identifiers.keySet().iterator();
	}

	public void addAll(Map<String, Integer> map) {
		identifiers.putAll(map);
	}

	public void remove(String identifier) {
		identifiers.remove(identifier);
	}

	public void clear() {
		identifiers.clear();
	}

	public void add(String identifier, int severity) {
		identifiers.put(identifier, severity);
	}

	@Override
	protected boolean contains(Location each) {
		return identifiers.containsKey(each.getDocument());
	}

	public int getSeverity(String identifier) {
		return identifiers.get(identifier);
	}
	
}
