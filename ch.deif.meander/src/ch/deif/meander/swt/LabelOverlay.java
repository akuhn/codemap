package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public class LabelOverlay extends SWTLayer {

	private MapScheme<String> labelScheme;
	
	public LabelOverlay() {
		labelScheme = new MapScheme<String>() {
			@Override
			public String forLocation(Point location) {
				String name = location.getDocument();
				int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
				int lastDot = name.lastIndexOf('.');
				if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
				return name;
			}
		};
	}
	
	public LabelOverlay(MapScheme<String> scheme) {
		labelScheme = scheme;
	}

	@Override
	public void paintMap(MapInstance map, GC gc) {
		String fname = "Arial Narrow";
		Font basefont = new Font(gc.getDevice(), fname, 12, SWT.NORMAL);
		Color white = new Color(gc.getDevice(), 255, 255, 255);
		Color black = new Color(gc.getDevice(), 0, 0, 0);
		for (Location each: map.locations()) {
			String name = labelScheme.forLocation(each.getPoint());
			FontData[] fontData = basefont.getFontData();
			int height = (int) (Math.sqrt(each.getElevation()) * 2);
			for (FontData fd: fontData) fd.setHeight(height);
			Font font = new Font(gc.getDevice(), fontData);
			gc.setFont(font);
			gc.setAlpha(128);
			gc.setForeground(black);
			gc.drawText(name, each.px + 1, each.py + 1, SWT.DRAW_TRANSPARENT);
			gc.setAlpha(255);
			gc.setForeground(white);
			gc.drawText(name, each.px, each.py, SWT.DRAW_TRANSPARENT);
			font.dispose();
		}
		basefont.dispose();
		black.dispose();
		white.dispose();	
	}

}
