package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapInstance;

public class WaterVisualization implements Layer {

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		pg.noStroke();
		pg.fill(map.isGrayscale() ? 0xFFFFFFFF : MapColor.WATER.asRGB());
		pg.rect(0, 0, pg.width, pg.height);
	}

}
