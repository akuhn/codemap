package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSetting;
import ch.deif.meander.MapInstance.Pixel;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.HillshadeAlgorithm;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class HillshadeVisualization extends Layer {

	public static final MapSetting<Integer> COAST_LEVEL = MapSetting.define("COAST_LEVEL", 10);
	private MapScheme<MColor> colorScheme;
	
	
	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		assert map.width == pg.width;
		assert map.height == pg.height;
		map.get(DEMAlgorithm.class);
		map.get(HillshadeAlgorithm.class);
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel p: map.pixels()) {
			if (p.elevation() > map.get(COAST_LEVEL)) {
				double shading = p.hillshade();
				if (p.hasContourLine()) shading *= 0.5;
				pixels[index] = colorOf(map, p).scaledRGB(shading);
			}
			pg.set(p.px, p.py, 0);
			index++;
		}
		pg.updatePixels();
	}

	private MColor colorOf(MapInstance map, Pixel p) {
		if (colorScheme == null) {
			colorScheme = map.get(GRAYSCALE) ? MapScheme.with(MColor.GRAY_204) : MapScheme.with(MColor.HILLGREEN);
		}
		Location nearestNeighbor = map.nearestNeighbor(p.px, p.py);
		return colorScheme.forLocation(nearestNeighbor.getPoint());
	}

	public void setColorScheme(MapScheme<MColor> colorScheme) {
		this.colorScheme = colorScheme;
	}

}
