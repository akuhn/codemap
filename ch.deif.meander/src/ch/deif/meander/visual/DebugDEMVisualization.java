package ch.deif.meander.visual;

import processing.core.PGraphics;
import processing.core.PImage;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapInstance.Pixel;
import ch.deif.meander.ui.MeanderApplet;

public class DebugDEMVisualization extends Layer {

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		PImage img = new PImage(map.getWidth(), map.getWidth());
		int[] pixels = img.pixels;
		int index = 0;
		for (Pixel p: map.pixels()) {
			pixels[index++] = pg.color((int) (p.elevation() * 2.50));
		}
		img.updatePixels();
		pg.image(img, 0, 0);
	}

}
