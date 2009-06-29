package ch.deif.meander.viz;

import processing.core.PConstants;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MapSelection;

public class YouAreHereOverlay extends MapVisualization {

	public final MapSelection elements;
	
	public YouAreHereOverlay(Map map) {
		super(map);
		elements = new MapSelection();
	}

	public YouAreHereOverlay(MapSelection mapSelection) {
		// map will be set as soon as this overlay is added to Meander
		super(null);
		elements = mapSelection;
	}
	
	@Override
	public void draw(PGraphics pg) {
		elements.convertElements(getMap());
		pg.fill(255);
		pg.stroke(0);
		pg.strokeWeight(1);
		for (Location each: elements) drawLocation(pg, each);
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
