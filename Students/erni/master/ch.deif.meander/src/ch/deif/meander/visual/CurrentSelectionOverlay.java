package ch.deif.meander.visual;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;

public class CurrentSelectionOverlay extends Layer {
	
	protected final int SELECTION_SIZE = 12;
	protected final int POINT_STROKE = 3;
	protected final int BOX_STROKE = 2;	
	
	private Collection<Point> points;
	private Point dragStart;
	private Point dragStop;	

	public CurrentSelectionOverlay() {
		points = Collections.synchronizedSet(new HashSet<Point>());
	}

	@Override
	public void draw(MapInstance map, PGraphics pg) {
		draw(map, pg, null);
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		pg.noFill();
		pg.stroke(Color.RED.getRGB());
		
		drawSelectedPoints(pg);
		drawSelectionBox(pg);

		pg.strokeWeight(2);
		pg.stroke(Color.BLACK.getRGB());
	}
	
	private void drawSelectedPoints(PGraphics pg) {
		pg.strokeWeight(POINT_STROKE);		
		synchronized (points) {
			for (Point each: points) {
				pg.ellipse(each.x, each.y, SELECTION_SIZE, SELECTION_SIZE);
			}			
		}
	}
	
	private void drawSelectionBox(PGraphics pg) {
		if (!hasDragInput()) return;

		pg.stroke(Color.RED.getRGB());
		pg.strokeWeight(BOX_STROKE);
		int deltaX = dragStop.x - dragStart.x;
		int deltaY = dragStop.y - dragStart.y;
		pg.rect(dragStart.x, dragStart.y, deltaX, deltaY);
	}
	

	private boolean hasDragInput() {
		return dragStart != null && dragStop != null;
	}	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		Point point = e.getPoint();
		if (!e.isControlDown()) {
			clearPoints();
		}
		if (e.getClickCount() == 2) {
			NearestNeighbor nn = new MaxDistNearestNeighbor(getMap(), getWidth() / 10).forLocation(point);
			if (nn.hasResult()) {
				addSelection(nn.point());
				events().doubleClicked(nn.location());
			}
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			// button1 is 1st mouse button
			NearestNeighbor nn = new MaxDistNearestNeighbor(getMap(), getWidth() / 10).forLocation(point);
			if (nn.hasResult()) {
				addSelection(nn.point());
				events().selectionChanged(nn.location());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// button3 is 2nd mouse button
			NearestNeighbor nn = new NearestNeighbor(getMap()).forLocation(point);
			addSelection(nn.point());
			events().selectionChanged(nn.location());
		}
		unsetSelectionBox();	
	}

	private void clearPoints() {
		synchronized (points) {
			points.clear();				
		}
	}
	
	//@Override
	public void updateSelection(List<String> handleIdentifiers) {
		super.updateSelection(handleIdentifiers);
		clearPoints();
		addSelection(handleIdentifiers);		
	}
	
	//@Override
	public void addSelection(List<String> handleIdentifiers) {
		synchronized (points) {
			for (Location each: getMap().locations()) {
				if (handleIdentifiers.contains(each.getIdentifier())) {
					points.add(new Point(each.px(), each.py()));
				}
			}			
		}
	}
	
	//@Override
	public void indicesSelected(int[] indices) {
		clearPoints();
		List<Location> locations = new ArrayList<Location>();
		synchronized (points) {
			for (int index: indices) {
				Location location = getMap().locationAt(index);
				locations.add(location);
				points.add(new Point(location.px(), location.py()));
			}			
		}
	}
	
	private void handleDragSelection() {
		List<Location> selected = new ArrayList<Location>();
		clearPoints();
		synchronized (points) {
			for (Location each: getMap().locations()) {
				int x = (int) Math.round(each.x() * getWidth());
				int y = (int) Math.round(each.y() * getWidth());
				if (x < dragStop.x && x > dragStart.x && y < dragStop.y && y > dragStart.y) {
					selected.add(each);
					points.add(new Point(x, y));
				}
			}
		}
		events().selectionChanged(selected.toArray(new Location[0]));
	}
	
	private void ensureDragPointOrder() {
		int minX = Math.min(dragStart.x, dragStop.x);
		int minY = Math.min(dragStart.y, dragStop.y);
		int maxX = Math.max(dragStart.x, dragStop.x);
		int maxY = Math.max(dragStart.y, dragStop.y);
		dragStart = new Point(minX, minY);
		dragStop = new Point(maxX, maxY);
	}
	
	private void unsetSelectionBox() {
		dragStart = null;
		dragStop = null;
	}	
	
	private void addSelection(Point point) {
		synchronized (points) {
			points.add(point);			
		}
	}
	
	//@Override
	public void mouseDragStarted(Point dragStart) {
		this.dragStart = dragStart;
	}
	
	//@Override
	public void mouseDraggedTo(Point dragStop) {
		this.dragStop = dragStop;
	}
	
	//@Override
	public void mouseDragStopped() {
		ensureDragPointOrder();
		handleDragSelection();
		unsetSelectionBox();		
	}

}
