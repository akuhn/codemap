package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapColor;
import ch.deif.aNewMeander.MapConfigurationWithSize;
import ch.deif.aNewMeander.MapConfigurationWithSize.Pixel;

public class HillshadeVisualization implements Layer {

	@Override
	public void draw(MapConfigurationWithSize map, PGraphics pg) {
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
			}
			index++;
		}
		pg.updatePixels();
	}

	private MapColor colorOf(MapConfigurationWithSize map, Pixel p) {
		return map.isGrayscale() ? MapColor.GRAY_204 : p.nearestNeighborColor();
	}

}
