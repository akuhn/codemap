package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapInstance;
import ch.deif.aNewMeander.MapInstance.Pixel;

public class HillshadeVisualization implements Layer {

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		assert map.width == pg.width;
		assert map.height == pg.height;
		map.needElevationModel();
		map.needHillshading();
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel p: map.pixels()) {
			if (p.elevation() > map.getBeachContourLevel()) {
				double shading = p.hillshade();
				if (p.hasContourLine()) shading *= 0.5;
				pixels[index] = colorOf(map, p).scaledRGB(shading);
				//pg.set(p.px, p.py, colorOf(map, p).scaledRGB(shading));
			}
			pg.set(p.px, p.py, 0);
			index++;
		}
		pg.updatePixels();
	}

	private MapColor colorOf(MapInstance map, Pixel p) {
		return map.isGrayscale() ? MapColor.GRAY_204 : p.nearestNeighborColor();
	}

}
