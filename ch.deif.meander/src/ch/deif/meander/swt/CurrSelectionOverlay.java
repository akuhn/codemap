package ch.deif.meander.swt;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;


public class CurrSelectionOverlay extends SelectionOverlay {

	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 3;
	protected final int BOX_STROKE = 2;
	
	public static final String EVT_DOUBLE_CLICKED = "doubleClicked";
	public static final String EVT_SELECTION_CHANGED = "selectionChanged";

	private boolean isDragging = false;
	private Point dragStart;
	private Point dragStop;

	@Override
	public void paintBefore(MapInstance map, GC pg) {
		Device device = pg.getDevice();
		Color red = new Color(device, 255, 0, 0);
		pg.setLineWidth(POINT_STROKE);
		pg.setForeground(red);
		if (isDragging) {
			updateSelection();
			int deltaX = dragStop.x - dragStart.x;
			int deltaY = dragStop.y - dragStart.y;
			pg.drawRectangle(dragStart.x, dragStart.y, deltaX, deltaY);
		}
	}

	@Override
	public void dragDetected(DragDetectEvent e) {
		
		System.out.println("CurrSelectionOverlay.dragDetected()");
		
		isDragging = true;
		dragStop = dragStart = new Point(e.x, e.y);	
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		Location neighbor = getRoot().map.nearestNeighbor(e.x, e.y);
		System.out.println("double clicked on: " + neighbor.getDocument());
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

	@Override
	public void paintChild(GC gc, Location each) {
		gc.drawOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
				SELECTION_SIZE, SELECTION_SIZE);
	}

}
