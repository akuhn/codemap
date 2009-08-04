package ch.deif.meander.swt;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.DEMAlgorithm;


public class CurrSelectionOverlay extends SelectionOverlay {

	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 3;
	protected final int BOX_STROKE = 2;
	
	public static final String EVT_DOUBLE_CLICKED = "doubleClicked";
	public static final String EVT_SELECTION_CHANGED = "selectionChanged";

	private boolean isDragging = false;
	private Point dragStart;
	private Point dragStop;
	
	public CurrSelectionOverlay() {}	

	@Override
	public void dragDetected(DragDetectEvent e) {
		isDragging = true;
		dragStop = dragStart = new Point(e.x, e.y);	
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		Location neighbor = getRoot().map.nearestNeighbor(e.x, e.y);
		fireEvent(EVT_DOUBLE_CLICKED, neighbor);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (!isDragging) return;
		dragStop = new Point(e.x, e.y);	
		redraw();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (isDragging) {
			updateSelection();			
		}
		isDragging = false;
		redraw();
		fireEvent(EVT_SELECTION_CHANGED, selection);
	}

	@Override
	public void paintBefore(MapInstance map, GC gc) {
		Device device = gc.getDevice();
		Color red = device.getSystemColor(SWT.COLOR_LIST_SELECTION);
		gc.setForeground(red);
		gc.setBackground(red);
		gc.setLineWidth(POINT_STROKE);
		if (isDragging) {
			updateSelection();
			int deltaX = dragStop.x - dragStart.x;
			int deltaY = dragStop.y - dragStart.y;
			gc.drawRectangle(dragStart.x, dragStart.y, deltaX, deltaY);
		}
		gc.setAlpha(128);
	}

	@Override
	public void paintChild(MapInstance map, GC gc, Location each) {
		int r = (int) (each.getElevation() * 2 * map.getWidth() / DEMAlgorithm.MAGIC_VALUE);
		gc.fillOval(each.px - r, each.py - r, r * 2, r * 2);
	}

	private void updateSelection() {
		int minX = Math.min(dragStart.x, dragStop.x);
		int minY = Math.min(dragStart.y, dragStop.y);
		int maxX = Math.max(dragStart.x, dragStop.x);
		int maxY = Math.max(dragStart.y, dragStop.y);		
		Collection<String> ids = new ArrayList<String>();
		for (Location each : getRoot().map.locations()) {
			if (each.px < maxX && each.px > minX && each.py < maxY && each.py > minY) {			
				ids.add(each.getDocument());
			}
		}
		getSelection().replaceWith(ids);
	}

}
