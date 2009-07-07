package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSetting;
import ch.deif.meander.MapInstance.Pixel;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.util.MColor;

public class ShoreVizualization extends Layer {

	public static final MapSetting<Integer> WATER_LEVEL = MapSetting.define("WATER_LEVEL", 2);
	
	@Override
	public void draw(MapInstance map, PGraphics pg) {
		map.get(DEMAlgorithm.class);
		MColor color = map.get(GRAYSCALE) ? new MColor(204,204,204) : MColor.SHORE;
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel each: map.pixels()) {
			if (each.elevation() > map.get(WATER_LEVEL)) { 
				pixels[index] = color.asRGB();
			}
			// TODO include "also show" to show shores of another map, eg to indicate atlantis
			index++;
		}
		pg.updatePixels();
	}

}
