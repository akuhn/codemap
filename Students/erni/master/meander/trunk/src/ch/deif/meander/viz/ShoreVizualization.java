package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Colors;
import ch.deif.meander.Map;
import ch.deif.meander.Map.Pixel;

public class ShoreVizualization extends MapVisualization {

	public ShoreVizualization(Map map) {
		super(map);
		map.needElevationModel();
	}

	@Override
	public void draw(PGraphics pg) {
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel each: map.pixels()) {
			if (each.elevation() > map.getParameters().waterHeight) 
				pixels[index] = Colors.SHORE.asRGB();
			index++;
		}
		pg.updatePixels();
	}

}
