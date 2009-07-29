package ch.deif.meander.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.DEMAlgorithm;

public class ShoreLayer extends SWTLayer {

	@Override
	public void paintMap(MapInstance map, GC gc) {
		float[][] DEM = map.get(DEMAlgorithm.class);
		Display display = Display.getCurrent();
		Color shore = new Color(display, 92, 142, 255);
		gc.setForeground(shore);
		Rectangle rect = new Rectangle(0, 0, map.width, map.height);
		rect.intersect(gc.getClipping());
		for (int x = rect.x; x < (rect.x + rect.width); x++) {
			for (int y = rect.y; y < (rect.y + rect.height); y++) {
				if (DEM[x][y] > 2) gc.drawPoint(x, y);
			}
		}
		shore.dispose();
	}
	
}
