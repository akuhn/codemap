package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public class LabelOverlay extends SWTLayer {

	private MapScheme<String> NAME = new MapScheme<String>() {
		@Override
		public String forLocation(Point location) {
			String name = location.getDocument().getIdentifier();
			int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
			int lastDot = name.lastIndexOf('.');
			if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
			return name;
		}
	};

	Font font;
	
	@Override
	public void paintMap(MapInstance map, GC gc) {
		//if (true == true) return;
		//gc.setTextAntialias(SWT.OFF);
		
		String fname = "Arial Narrow";
		Display display = Display.getCurrent();
		//if (font == null) font = new Font(display, fname, 20, SWT.NORMAL);
		Color white = new Color(display, 255, 255, 255);
		Color black = new Color(display, 0, 0, 0);
		//gc.setFont(font);
		gc.setForeground(white);
		for (Location each: map.locations()) {
			String name = NAME.forLocation(each.getPoint());
			//Font font = new Font(display, fname, (int) (Math.sqrt(each.getElevation()) * 2), SWT.NORMAL);
			//gc.setFont(font);
			//gc.setAlpha(128);
			//gc.setForeground(black);
			//gc.drawText(name, each.px + 1, each.py + 1, SWT.DRAW_TRANSPARENT);
			//gc.setAlpha(255);
			gc.drawText(name, each.px, each.py, 0*SWT.DRAW_TRANSPARENT);
		}
		//font.dispose();
		black.dispose();
		white.dispose();
		
	}

}
