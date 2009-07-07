package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;

public abstract class MapSelectionOverlay implements Layer {

	private MapSelection selection;

	public void setSelection(MapSelection selection) {
		this.selection = selection;
	}

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		for (Location each: selection.locationsOn(map)) {
			drawLocation(pg, each);
		}
	}	
	
	public abstract void drawLocation(PGraphics pg, Location each);

	public MapSelection getSelection() {
		return selection;
	}

}
