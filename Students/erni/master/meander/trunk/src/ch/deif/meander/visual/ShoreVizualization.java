package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.MapColor;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapInstance.Pixel;

public class ShoreVizualization implements Layer {

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		map.needElevationModel();
		MapColor color = map.isGrayscale() ? new MapColor(204,204,204) : MapColor.SHORE;
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel each: map.pixels()) {
			if (each.elevation() > map.getWaterContourLevel()) 
				pixels[index] = color.asRGB();
			// TODO include "also show" to show shores of another map, eg to indicate atlantis
			index++;
		}
		pg.updatePixels();
	}

}
