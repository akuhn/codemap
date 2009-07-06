package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapColor;
import ch.deif.meander.Map;
import ch.deif.meander.viz.MapVisualization;

public class WaterVisualization extends MapVisualization {

	public WaterVisualization(Map map) {
		super(map);
	}

	@Override
	public void draw(PGraphics pg) {
		pg.noStroke();
		pg.fill(getMap().getParameters().blackAndWhite ? 0xFFFFFFFF : MapColor.WATER.asRGB());
		pg.rect(0, 0, getWidth(), getWidth());
	}

}
