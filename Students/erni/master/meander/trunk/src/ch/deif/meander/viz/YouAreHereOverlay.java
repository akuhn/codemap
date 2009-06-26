package ch.deif.meander.viz;

import processing.core.PConstants;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.Map;

public class YouAreHereOverlay extends MapVisualization {

	private Location here;
	
	public YouAreHereOverlay(Map map) {
		super(map);
	}

	@Override
	public void draw(PGraphics pg) {
		if (here == null) return;
		pg.fill(255);
		pg.stroke(0);
		pg.pushMatrix();
		pg.translate(here.px(), here.py());
		pg.beginShape();
		pg.vertex(0, 0);
		pg.vertex(6, -6);
		pg.vertex(12, -6);
		pg.vertex(12, -24);
		pg.vertex(-12, -24);
		pg.vertex(-12, -6);
		pg.vertex(-6, -6);
		pg.endShape(PConstants.CLOSE);
		pg.popMatrix();
	}

	public void setHere(Location location) {
		here = location;
	}
	
}
