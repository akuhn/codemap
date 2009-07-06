package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.Location;
import ch.deif.aNewMeander.LocationWithSize;
import ch.deif.aNewMeander.MapConfigurationWithSize;

public class SketchVisualization implements Layer {

	// TODO make sure circle size has same diameter as coastline of shaded hills

	@Override
	public void draw(MapConfigurationWithSize map, PGraphics pg) {
		float width = map.getWidth();
		pg.background(204);
		pg.stroke(0);
		pg.noFill();
		pg.smooth();
		for (Location each: map.locations()) {
			float x = (float) (each.getX() * width);
			float y = (float) (each.getY() * width);
			float r = (float) (((LocationWithSize) each).getElevation() / 500 * width);
			pg.ellipse(x, y, r, r);
		}
	}

}
