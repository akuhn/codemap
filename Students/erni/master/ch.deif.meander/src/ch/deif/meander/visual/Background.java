package ch.deif.meander.visual;

import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class Background extends Composite<Layer> {

	private PImage background;

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		if (pg instanceof PGraphicsJava2D) drawOnJava2D(map, (PGraphicsJava2D) pg);
		else this.drawChildren(map, pg, pa);
	}

	private void drawOnJava2D(MapInstance map, PGraphicsJava2D pg) {
		if (background == null || background.width != map.width || background.height != map.height) {
			this.drawChildren(map, pg, null);
			background = pg.get();
		}
		else {
			pg.image(background, 0, 0);
		}
	}
	
}
