package org.codemap.plugin.marker;

import java.awt.Color;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.visual.MapSelectionOverlay;

public class MarkersOverlay extends MapSelectionOverlay {
	
	protected final int SELECTION_SIZE = 15;
	protected final int SELECTION_STROKE = 0;

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		pg.strokeWeight(SELECTION_STROKE);
		pg.stroke(Color.ORANGE.getRGB());
		pg.fill(Color.ORANGE.getRGB());
		super.draw(map, pg, pa);
	}

	@Override
	public void drawLocation(PGraphics pg, Location each) {
		pg.ellipse(each.px, each.py, SELECTION_SIZE, SELECTION_SIZE);
	}

}
