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
		gc.setLineWidth(1);
	}

	@Override
	public void paintChild(GC gc, Location each) {
		gc.fillOval(each.px - 8, each.py - 6, each.px + 8, each.py + 6);
		gc.drawOval(each.px - 8, each.py - 6, each.px + 8, each.py + 6);
	}

}
