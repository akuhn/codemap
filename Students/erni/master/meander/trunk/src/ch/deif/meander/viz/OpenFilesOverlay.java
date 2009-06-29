package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;

public class OpenFilesOverlay extends MapSelectionOverlay {
	
	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 1;	

	public OpenFilesOverlay(MapSelection mapSelection) {
		super(mapSelection);
	}

	@Override
	public void draw(PGraphics pg) {
		pg.strokeWeight(1);
		pg.fill(255);
		pg.stroke(0);
		
		super.draw(pg);
	}

	@Override
	public void drawLocation(PGraphics pg, Location each) {
		pg.ellipse(each.px(), each.py(), SELECTION_SIZE+2, SELECTION_SIZE);
	}

}
