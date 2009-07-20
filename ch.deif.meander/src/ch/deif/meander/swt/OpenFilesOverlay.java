package ch.deif.meander.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;

public class OpenFilesOverlay extends SelectionOverlay {

	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 1;	

	@Override
	public void paintBefore(MapInstance map, GC gc) {
		Display display = Display.getCurrent();
		gc.setForeground(new Color(display, 0, 0, 0));
		gc.setBackground(new Color(display, 255, 255, 255));
		gc.setLineWidth(POINT_STROKE);
	}

	@Override
	public void paintChild(GC gc, Location each) {
		gc.fillOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
				SELECTION_SIZE, SELECTION_SIZE);
		gc.drawOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
				SELECTION_SIZE, SELECTION_SIZE);
	}

}
