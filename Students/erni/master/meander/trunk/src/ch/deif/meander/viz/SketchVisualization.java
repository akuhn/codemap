package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.Map;

public class SketchVisualization extends MapVisualization {

	// TODO make sure circle size has same diameter as coastline of shaded hills

	public SketchVisualization(Map map) {
		super(map);
	}

	@Override
	public void draw(PGraphics pg) {
		float width = getMap().getWidth();
		pg.background(204);
		pg.stroke(0);
		pg.noFill();
		pg.smooth();
		for (Location each: getMap().locations()) {
			float x = (float) (each.x() * width);
			float y = (float) (each.y() * width);
			float r = (float) (each.elevation() / 500 * width);
			pg.ellipse(x, y, r, r);
		}
	}

}
