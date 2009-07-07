package ch.deif.meander.visual;

import processing.core.PConstants;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;

public class YouAreHereOverlay extends MapSelectionOverlay {

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		pg.fill(255);
		pg.stroke(0);
		pg.strokeWeight(1);
		
		super.draw(map, pg);
	}
	
	public void drawLocation(PGraphics pg, Location each) {
		pg.pushMatrix();
		pg.translate(each.px, each.py);
		pg.beginShape();
		pg.vertex(0, 0);
		//pg.vertex(6, -6);
		//pg.vertex(12, -6);
		pg.vertex(12, -24);
		pg.vertex(-12, -24);
		//pg.vertex(-12, -6);
		//pg.vertex(-6, -6);
		pg.endShape(PConstants.CLOSE);
		pg.popMatrix();
	}

}
