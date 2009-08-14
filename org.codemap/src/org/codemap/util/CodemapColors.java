package org.codemap.util;

import java.util.HashMap;
import java.util.Map;

import ch.deif.meander.Point;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

// FIXME, remove me?

public class CodemapColors extends MapScheme<MColor> {
	
	private Map<String, MColor> forLocation;

	public CodemapColors() {
		super(MColor.HILLGREEN);
		forLocation = new HashMap<String, MColor>();
	}
	
	public void setColor(String identifier, MColor color) {
		forLocation.put(identifier, color);
	}
	
	public void clearColors() {
		forLocation.clear();
	}

	@Override
	public MColor forLocation(Point location) {
		String identifier = location.getDocument();
		if (forLocation.containsKey(identifier)) return forLocation.get(identifier);
		return super.forLocation(location);
	}
	
	

}
