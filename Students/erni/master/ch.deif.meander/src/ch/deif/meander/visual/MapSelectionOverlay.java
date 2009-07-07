package ch.deif.meander.visual;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.ui.MeanderApplet;

public abstract class MapSelectionOverlay extends Layer {

	private MapSelection selection;

	public MapSelectionOverlay setSelection(MapSelection selection) {
		this.selection = selection;
		return this;
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		draw(map, pg, pa);
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		for (Location each: selection.locationsOn(map)) {
			drawLocation(pg, each);
		}
	}	
	
	public abstract void drawLocation(PGraphics pg, Location each);

	public MapSelection getSelection() {
		return selection;
	}

}
