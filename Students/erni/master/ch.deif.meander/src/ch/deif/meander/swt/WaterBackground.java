package ch.deif.meander.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.MapInstance;

public class WaterBackground extends SWTLayer {

	public void paintMap(MapInstance map, GC gc) {
		Display display = Display.getCurrent();
		Color blue = new Color(display, 0, 0, 255);
		gc.setBackground(blue);
		gc.fillRectangle(gc.getClipping());
		blue.dispose();
	}
	
}
