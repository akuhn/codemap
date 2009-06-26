package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Map;
import ch.deif.meander.internal.Colors;

public class WaterVisualization extends MapVisualization {

	public WaterVisualization(Map map) {
		super(map);
	}

	@Override
	public void draw(PGraphics pg) {
		pg.noStroke();
		pg.fill(map.getParameters().blackAndWhite ? 0xFFFFFFFF : Colors.WATER.asRGB());
		pg.rect(0, 0, getWidth(), getWidth());
	}

}
