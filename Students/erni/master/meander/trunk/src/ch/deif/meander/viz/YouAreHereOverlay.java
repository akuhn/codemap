package ch.deif.meander.viz;

import processing.core.PConstants;
import processing.core.PGraphics;
import ch.deif.meander.Location;

public class YouAreHereOverlay extends MapSelectionOverlay {

	public YouAreHereOverlay() {
		super(mapSelection);
	}
	@Override
	public void draw(PGraphics pg) {
		pg.fill(255);
		pg.stroke(0);
		pg.strokeWeight(1);
		
		super.draw(pg);
	}
	
	public void drawLocation(PGraphics pg, Location each) {
		pg.pushMatrix();
		pg.translate(each.px(), each.py());
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
