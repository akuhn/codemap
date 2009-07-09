package ch.deif.meander.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;

public class YouAreHereOverlay extends SelectionOverlay {

	@Override
	public void paintBefore(MapInstance map, GC gc) {
		Display display = Display.getCurrent();
		gc.setForeground(new Color(display, 0, 0, 0));
		gc.setBackground(new Color(display, 255, 255, 255));
		gc.setLineWidth(1);
	}
	
	@Override
	public void paintChild(GC gc, Location each) {
		int[] pointArray = new int[] { 
				each.px, each.py,
				each.px + 12, each.py - 24,
				each.px - 12, each.py - 24,
		};
		gc.fillPolygon(pointArray);
		gc.drawPolygon(pointArray);
	}

}