package ch.deif.meander.visual;

import processing.core.PConstants;
import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class Background extends Composite<Layer> {

	private PGraphics background;

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		if (background == null || background.width != map.width || background.height != map.height) {
			background = pa.createGraphics(map.width, map.height, PConstants.JAVA2D);
			this.drawChildren(map, background, null);
		}
		pg.image(background, 0, 0);
	}
	
}
