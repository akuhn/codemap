package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;

public class YouAreHereOverlay extends SelectionOverlay {

	private static final int GAP = 4;
	private static final int ARROW_WIDTH = 8;
	private static final int ARROW_HEIGHT = 10;
	private static final int PADDING_X = 6;
	private static final int PADDING_Y = 1;
	
	
	@Override
	public void paintBefore(MapInstance map, GC gc) {
		Device device = gc.getDevice();
		gc.setForeground(device.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		gc.setBackground(device.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		gc.setAlpha(255);
		gc.setLineWidth(1);
	}
	
	@Override
	public void paintChild(MapInstance map, GC gc, Location each) {
		Device device = gc.getDevice();
		gc.setFont(device.getSystemFont());
		String name = each.getName();
		Point e = gc.stringExtent(name);
		e.x += PADDING_X * 2;
		e.y += PADDING_Y * 2;
		int[] polygon = new int[] { 
				0, 0,
				e.x, 0,
				e.x, e.y,
				(e.x + ARROW_WIDTH) / 2, e.y,
				e.x / 2, e.y + ARROW_HEIGHT,
				(e.x - ARROW_WIDTH) / 2, e.y,
				0, e.y };
		Transform t = new Transform(device);
		gc.getTransform(t);
		t.translate(each.px - e.x/2, each.py - e.y - ARROW_HEIGHT - GAP);
		gc.setTransform(t);
		gc.fillPolygon(polygon);
		gc.drawPolygon(polygon);
		gc.drawText(name, PADDING_X, PADDING_Y);
		t.dispose();
		
		// FIXME do I have to save and restore the previous transform?
		
		// TODO learn from class RenameInformationPopup how to open a custom popup, dream on...
		
	}

}