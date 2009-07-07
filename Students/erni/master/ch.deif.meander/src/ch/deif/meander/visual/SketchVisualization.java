package ch.deif.meander.visual;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class SketchVisualization extends Layer {

	// TODO make sure circle size has same diameter as coastline of shaded hills

	@Override
	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		draw(map, pg, pa);
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		float width = map.getWidth();
		pg.background(204);
		pg.stroke(0);
		pg.noFill();
		pg.smooth();
		for (Location each: map.locations()) {
			float x = (float) (each.px);
			float y = (float) (each.py);
			float r = (float) (each.getElevation() / 500 * width);
			pg.ellipse(x, y, r, r);
		}
	}

}
