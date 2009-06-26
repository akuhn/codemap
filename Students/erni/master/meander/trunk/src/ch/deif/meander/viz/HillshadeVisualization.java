package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Colors;
import ch.deif.meander.Map;
import ch.deif.meander.Map.Pixel;

public class HillshadeVisualization extends MapVisualization {

	private boolean blackAndWhite;

	public HillshadeVisualization(Map map) {
		super(map);
		map.needHillshading();
		blackAndWhite = map.getParameters().blackAndWhite;
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
				pixels[index] = colorOf(p).scaledRGB(shading);
			}
			index++;
		}
		pg.updatePixels();
	}

	private Colors colorOf(Pixel p) {
		return blackAndWhite ? Colors.GRAY_204 : p.nearestNeighborColor();
	}

}
