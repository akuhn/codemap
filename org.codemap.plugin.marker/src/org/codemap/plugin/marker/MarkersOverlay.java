package org.codemap.plugin.marker;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.swt.SelectionOverlay;

public class MarkersOverlay extends SelectionOverlay {
	
	protected final int SELECTION_SIZE = 15;
	
	@Override
	public void paintBefore(MapInstance map, GC gc) {
		Device device = gc.getDevice();
		Color orange = new Color(device, 255, 200, 0);
		gc.setBackground(orange);
	}

	@Override
	public void paintChild(GC gc, Location each) {
		gc.fillOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
				SELECTION_SIZE, SELECTION_SIZE);
	}

}
