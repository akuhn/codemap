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
	
	private boolean isDragging = false;
	private Point dragStart;
	private Point dragStop;	

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		pg.noFill();
		pg.stroke(Color.RED.getRGB());
		if (pa != null) handleEvents(map, pa);
		if (isDragging) updateSelection(map);
		super.draw(map, pg, pa);
		drawSelectionBox(pg);
	}
	
	private void handleEvents(MapInstance map, MeanderApplet pa) {
		if (isDragging) handleDragging(map, pa);
		else handleNonDragging(map, pa);
	}

	private void handleNonDragging(MapInstance map, MeanderApplet pa) {
		if (pa.mouseEvent == null) return;
		if (pa.mouseEvent.getID() == MouseEvent.MOUSE_DRAGGED) {
			isDragging = true;
			dragStop = dragStart = new Point(pa.mouseX, pa.mouseY);
		}
	}

	private void handleDragging(MapInstance map, MeanderApplet pa) {
		dragStop = new Point(pa.mouseX, pa.mouseY);
		if (pa.mouseEvent == null || pa.mouseEvent.getID() != MouseEvent.MOUSE_DRAGGED) {
			isDragging = false;
		}
	}
	
	private void drawSelectionBox(PGraphics pg) {
		if (!isDragging) return;
		System.out.println("...");
		pg.stroke(Color.RED.getRGB());
		pg.strokeWeight(BOX_STROKE);
		int deltaX = dragStop.x - dragStart.x;
		int deltaY = dragStop.y - dragStart.y;
		pg.rect(dragStart.x, dragStart.y, deltaX, deltaY);
	}
	

//	@Override
//	public void mouseClicked(MouseEvent e) {
//		
//		Point point = e.getPoint();
//		if (!e.isControlDown()) {
//			clearPoints();
//		}
//		if (e.getClickCount() == 2) {
//			NearestNeighbor nn = new MaxDistNearestNeighbor(getMap(), getWidth() / 10).forLocation(point);
//			if (nn.hasResult()) {
//				addSelection(nn.point());
//				events().doubleClicked(nn.location());
//			}
//		} else if (e.getButton() == MouseEvent.BUTTON1) {
//			// button1 is 1st mouse button
//			NearestNeighbor nn = new MaxDistNearestNeighbor(getMap(), getWidth() / 10).forLocation(point);
//			if (nn.hasResult()) {
//				addSelection(nn.point());
//				events().selectionChanged(nn.location());
//			}
//		} else if (e.getButton() == MouseEvent.BUTTON3) {
//			// button3 is 2nd mouse button
//			NearestNeighbor nn = new NearestNeighbor(getMap()).forLocation(point);
//			addSelection(nn.point());
//			events().selectionChanged(nn.location());
//		}
//		unsetSelectionBox();	
//	}

	private void updateSelection(MapInstance map) {
		Collection<String> ids = new ArrayList<String>();
		for (Location each: map.locations()) {
			if (each.px < dragStop.x && each.px > dragStart.x && 
					each.py < dragStop.y && each.py > dragStart.y) {
				ids.add(each.getIdentifier());
			}
		}
		getSelection().replaceWith(ids);
	}
	
	private void ensureDragPointOrder() {
		int minX = Math.min(dragStart.x, dragStop.x);
		int minY = Math.min(dragStart.y, dragStop.y);
		int maxX = Math.max(dragStart.x, dragStop.x);
		int maxY = Math.max(dragStart.y, dragStop.y);
		dragStart = new Point(minX, minY);
		dragStop = new Point(maxX, maxY);
	}
	
	@Override
	public void drawLocation(PGraphics pg, Location each) {
		pg.ellipse(each.px, each.py, SELECTION_SIZE, SELECTION_SIZE);
	}

}
