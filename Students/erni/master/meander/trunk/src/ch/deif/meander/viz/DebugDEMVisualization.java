package ch.deif.meander.viz;

import processing.core.PGraphics;
import processing.core.PImage;
import ch.deif.meander.Map;
import ch.deif.meander.Map.Pixel;

public class DebugDEMVisualization extends MapVisualization {

	public DebugDEMVisualization(Map map) {
		super(map);
	}

	@Override
	public void draw(PGraphics pg) {
		PImage img = new PImage(getMap().getWidth(), getMap().getWidth());
		int[] pixels = img.pixels;
		int index = 0;
		for (Pixel p: getMap().pixels()) {
			pixels[index++] = pg.color((int) (p.elevation() * 2.50));
		}
		img.updatePixels();
		pg.image(img, 0, 0);
	}

}
