package org.codemap.plugin.search;

import java.awt.Color;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.visual.MapSelectionOverlay;

public class SearchResultsOverlay extends MapSelectionOverlay {
	
	protected final int SELECTION_SIZE = 15;
	protected final int SELECTION_STROKE = 3;

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		pg.stroke(Color.BLUE.getRGB());
		pg.strokeWeight(SELECTION_STROKE);
		pg.fill(Color.WHITE.getRGB());
		super.draw(map, pg, pa);
	}

	@Override
	public void drawLocation(PGraphics pg, Location each) {
		pg.ellipse(each.px, each.py, SELECTION_SIZE, SELECTION_SIZE);
	}
	

}
