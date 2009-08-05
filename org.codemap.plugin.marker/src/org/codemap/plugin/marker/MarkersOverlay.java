package org.codemap.plugin.marker;

import static org.codemap.util.Icons.ERROR;
import static org.codemap.util.Icons.INFO;
import static org.codemap.util.Icons.WARNING;

import org.codemap.util.Icons;
import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import ch.deif.meander.AbstractMapSelection;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.swt.SelectionOverlay;

public class MarkersOverlay extends SelectionOverlay {

	private MarkerSelection markerSelection;

	@Override
	public AbstractMapSelection getSelection() {
		return markerSelection;
	}

	@Override
	public void paintBefore(MapInstance map, GC gc) {
		Device device = gc.getDevice();
		Color orange = new Color(device, 255, 200, 0);
		gc.setBackground(orange);
	}

	@Override
	public void paintChild(MapInstance map, GC gc, Location each) {
		int severity = markerSelection.getSeverity(each.getDocument());
		Image image = getImage(severity);
		Rectangle offset = image.getBounds();		
		int offset_x = offset.width/2;
		int offset_y = offset.height/2;		
		gc.drawImage(image, each.px - offset_x, each.py - offset_y);
	}

	private Image getImage(int severity) {
		switch (severity) {
		case IMarker.SEVERITY_ERROR:
			return Icons.getImage(ERROR);
		case IMarker.SEVERITY_WARNING:
			return Icons.getImage(WARNING);
		default:
			return Icons.getImage(INFO);
		}
	}

	public void setMarkerSelection(MarkerSelection selection) {
		markerSelection = selection;
	}

}
