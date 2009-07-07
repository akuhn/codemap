package ch.deif.meander.visual;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.util.MColor;

public class WaterVisualization extends Layer {

	@Override
	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		pg.noStroke();
		pg.fill(map.get(GRAYSCALE) ? 0xFFFFFFFF : MColor.WATER.asRGB());
		pg.rect(0, 0, pg.width, pg.height);
	}

}
