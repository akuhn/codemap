package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Map;
import ch.deif.meander.Map.Pixel;

public class HillshadeVisualization extends MapVisualization {

	public HillshadeVisualization(Map map) {
		super(map);
	}

	@Override
	public void draw(PGraphics pg) {
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel p: map.pixels()) {
			if (p.elevation() > map.getParameters().beachHeight) {
				double shading = p.hillshade();
				if (p.hasContourLine()) shading *= 0.5;
				pixels[index] = p.nearestNeighborColor().scaledRGB(shading);
			}
			index++;
		}
		pg.updatePixels();
	}

}
