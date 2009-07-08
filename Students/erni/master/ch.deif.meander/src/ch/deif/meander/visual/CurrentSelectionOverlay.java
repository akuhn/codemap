package ch.deif.meander.visual;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class CurrentSelectionOverlay extends MapSelectionOverlay {

	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 3;
	protected final int BOX_STROKE = 2;
	
	public static final String EVT_DOUBLE_CLICKED = "doubleClicked";
	public static final String EVT_SELECTION_CHANGED = "selectionChanged";

	private boolean isDragging = false;
	private Point dragStart;
	private Point dragStop;

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		pg.noFill();
		pg.stroke(Color.RED.getRGB());
		if (pa != null)
			handleEvents(map, pa);
		if (isDragging)
			updateSelection(map);
		pg.strokeWeight(POINT_STROKE);
		super.draw(map, pg, pa);
		drawSelectionBox(pg);
	}

	private void handleEvents(MapInstance map, MeanderApplet pa) {
		if (isDragging)
			handleDragging(map, pa);
		else
			handleNonDragging(map, pa);
	}

	private void handleNonDragging(MapInstance map, MeanderApplet pa) {
		MouseEvent event = pa.mouseEvent;
		if (event == null) return;
		if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
			isDragging = true;
			dragStop = dragStart = new Point(pa.mouseX, pa.mouseY);
		} else if (event.getID() == MouseEvent.MOUSE_CLICKED) {
			if (! (event.getClickCount() == 2)) return;
			Point point = event.getPoint();
			Location neighbor = map.nearestNeighbor(point.x, point.y);
			System.out.println("double clicked on: " + neighbor.getIdentifier());
			pa.fireEvent(EVT_DOUBLE_CLICKED, this, neighbor);
		}
	}

	private void handleDragging(MapInstance map, MeanderApplet pa) {
		dragStop = new Point(pa.mouseX, pa.mouseY);
		if (pa.mouseEvent == null
				|| pa.mouseEvent.getID() != MouseEvent.MOUSE_DRAGGED) {
			isDragging = false;
		}
	}

	private void drawSelectionBox(PGraphics pg) {
		if (!isDragging)
			return;
		pg.stroke(Color.RED.getRGB());
		pg.strokeWeight(BOX_STROKE);
		int deltaX = dragStop.x - dragStart.x;
		int deltaY = dragStop.y - dragStart.y;
		pg.rect(dragStart.x, dragStart.y, deltaX, deltaY);
	}

	private void updateSelection(MapInstance map) {
		int minX = Math.min(dragStart.x, dragStop.x);
		int minY = Math.min(dragStart.y, dragStop.y);
		int maxX = Math.max(dragStart.x, dragStop.x);
		int maxY = Math.max(dragStart.y, dragStop.y);		
		
		
		Collection<String> ids = new ArrayList<String>();
		for (Location each : map.locations()) {
			if (each.px < maxX && each.px > minX && each.py < maxY && each.py > minY) {			
				ids.add(each.getIdentifier());
			}
		}
		getSelection().replaceWith(ids);
	}

	@Override
	public void drawLocation(PGraphics pg, Location each) {
		pg.ellipse(each.px, each.py, SELECTION_SIZE, SELECTION_SIZE);
	}

}
