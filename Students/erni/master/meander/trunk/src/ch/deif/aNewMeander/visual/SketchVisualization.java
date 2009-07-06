package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.Location;
import ch.deif.aNewMeander.MapInstance;

public class SketchVisualization implements Layer {

	// TODO make sure circle size has same diameter as coastline of shaded hills

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		float width = map.getWidth();
		pg.background(204);
		pg.stroke(0);
		pg.noFill();
		pg.smooth();
		for (Location each: map.locations()) {
			float x = (float) (each.getPx());
			float y = (float) (each.getPy());
			float r = (float) (each.getElevation() / 500 * width);
			pg.ellipse(x, y, r, r);
		}
	}

}
