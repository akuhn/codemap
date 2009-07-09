package ch.deif.meander.swt;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.ContourLineAlgorithm;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.internal.HillshadeAlgorithm;
import ch.deif.meander.util.SparseTrueBooleanList;

public class HillshadeLayer extends SWTLayer {

	public void paintMap(MapInstance map, GC gc) {
		float[][] DEM = map.get(DEMAlgorithm.class);
		double[][] shade = map.get(HillshadeAlgorithm.class);
		List<SparseTrueBooleanList> list = map.get(ContourLineAlgorithm.class);
		Display display = Display.getCurrent();
		Rectangle rect = new Rectangle(0, 0, map.width, map.height);
		rect.intersect(gc.getClipping());
		for (int x = rect.x; x < (rect.x + rect.width); x++) {
			for (int y = rect.y; y < (rect.y + rect.height); y++) {
				if (DEM[x][y] > 10) {
					double f = shade[x][y];
					if (list.get(x).get(y)) f *= 0.5;
					if (f < 0.0) f = 0.0;
					Color shore = new Color(display, (int) (196 * f), (int) (236 * f), 0);
					gc.setForeground(shore);
					gc.drawPoint(x, y);
					shore.dispose();
				}
			}
		}
	}
	
	
}
