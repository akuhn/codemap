package ch.deif.meander.visual;

import processing.core.PGraphics;
import processing.core.PImage;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class Background extends Composite<Layer> {

	private PImage background;

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		if (background == null || background.width != map.width || background.height != map.height) {
			this.drawChildren(map, pg, pa);
			background = pg.get();
		}
		else {
			pg.image(background, 0, 0);
		}
	}
	
}
