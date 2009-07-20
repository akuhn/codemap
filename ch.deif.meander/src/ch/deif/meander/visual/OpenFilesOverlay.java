package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class OpenFilesOverlay extends MapSelectionOverlay {
	
	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 1;	

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		pg.strokeWeight(1);
		pg.fill(255);
		pg.stroke(0);		
		super.draw(map, pg, pa);
	}

	@Override
	public void drawLocation(PGraphics pg, Location each) {
		pg.ellipse(each.px, each.py, SELECTION_SIZE+2, SELECTION_SIZE);
	}

}