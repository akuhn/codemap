package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapColor;
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
		for (Pixel p: getMap().pixels()) {
			if (p.elevation() > getMap().getParameters().beachHeight) {
				double shading = p.hillshade();
				if (p.hasContourLine()) shading *= 0.5;
				pixels[index] = colorOf(p).scaledRGB(shading);
			}
			index++;
		}
		pg.updatePixels();
	}

	private MapColor colorOf(Pixel p) {
		return blackAndWhite ? MapColor.GRAY_204 : p.nearestNeighborColor();
	}

}
