package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MapSelection;

public abstract class MapSelectionOverlay extends MapVisualization {

	private MapSelection elements;

	public MapSelectionOverlay(Map map) {
		super(map);
	}

	public MapSelectionOverlay(MapSelection mapSelection) {
		// map will be set as soon as this overlay is added to Meander
		super(null);
		elements = mapSelection;
	}

	@Override
	public void draw(PGraphics pg) {
		elements.registerMap(getMap());
		for (Location each: getElements()) drawLocation(pg, each);
	}	
	
	public abstract void drawLocation(PGraphics pg, Location each);

	public MapSelection getElements() {
		return elements;
	}

}
