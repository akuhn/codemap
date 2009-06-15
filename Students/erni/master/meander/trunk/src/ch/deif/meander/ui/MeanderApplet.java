package ch.deif.meander.ui;

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
import ch.deif.meander.Map;
import ch.deif.meander.MaxDistNearestNeighbor;
import ch.deif.meander.NearestNeighbor;
import ch.deif.meander.viz.MapVisualization;

@SuppressWarnings("serial")
public class MeanderApplet extends PApplet {

	protected final int SELECTION_SIZE = 10;
	protected final int POINT_STROKE = 2;
	protected final int BOX_STROKE = 2;

	private MapVisualization viz;
	private Collection<Point> points;

	private boolean needsRedraw;
	private PGraphics bg;

	private Point dragStart;
	private Point dragStop;

	private Events events;

	private static class Events {

		private MeanderEventListener listener;
		private MeanderApplet applet;

		public Events(MeanderApplet meanderApplet) {
			this.applet = meanderApplet;
		}

		public void addListener(MeanderEventListener listener) {
			assert this.listener == null;
			this.listener = listener;
		}

		public void removeListener(MeanderEventListener listener) {
			assert this.listener == listener;
			this.listener = null;
		}

		public void doubleClicked(final Location location) {
			if (listener != null) new Thread() {
				@Override
				public void run() {
					listener.doubleClicked(location);
				}
			}.start();
		}

		public void selectionChanged(final Location... locations) {
			if (listener != null) new Thread() {
				@Override
				public void run() {
					listener.selectionChanged(locations);
				}
			}.start();
		}

	}

	public MeanderApplet() {
		events = new Events(this);
		points = Collections.synchronizedSet(new HashSet<Point>());
	}

	public Events events() {
		return events;
	}

	public void addListener(MeanderEventListener listener) {
		events().addListener(listener);
	}

	public void removeListener(MeanderEventListener listener) {
		events.removeListener(listener);
	}

	@Override
	public void setup() {
		frameRate(25);
	}

	private void setupBackground() {
		bg.beginDraw();
		viz.draw(bg);
		bg.endDraw();
	}

	@Override
	public void draw() {
		if (!needsRedraw) return;

		smooth();
		noFill();
		strokeWeight(POINT_STROKE);
		drawBackground();
		drawSelectedPoints();
		drawSelectionBox();
		needsRedraw = false;
	}

	private void drawSelectionBox() {
		if (!hasDragInput()) return;

		stroke(Color.RED.getRGB());
		strokeWeight(BOX_STROKE);
		int deltaX = dragStop.x - dragStart.x;
		int deltaY = dragStop.y - dragStart.y;
		rect(dragStart.x, dragStart.y, deltaX, deltaY);
		strokeWeight(POINT_STROKE);
	}

	private void drawSelectedPoints() {
		for (Point each: points) {
			stroke(Color.RED.getRGB());
			ellipse(each.x, each.y, SELECTION_SIZE, SELECTION_SIZE);
		}
	}

	private void drawBackground() {
		image(bg, 0, 0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		Point point = e.getPoint();
		if (!e.isControlDown()) points.clear();
		if (e.getClickCount() == 2) {
			NearestNeighbor nn = new MaxDistNearestNeighbor(map(), width() / 10).forLocation(point);
			if (nn.hasResult()) {
				addSelection(nn.point());
				events().doubleClicked(nn.location());
			}
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			// button1 is 1st mouse button
			NearestNeighbor nn = new MaxDistNearestNeighbor(map(), width() / 10).forLocation(point);
			if (nn.hasResult()) {
				addSelection(nn.point());
				events().selectionChanged(nn.location());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// button3 is 2nd mouse button
			NearestNeighbor nn = new NearestNeighbor(map()).forLocation(point);
			addSelection(nn.point());
			events().selectionChanged(nn.location());
		}
		unsetSelectionBox();
		setNeedsRedraw();
	}

	private void addSelection(Point point) {
		points.add(point);
	}

	private void unsetSelectionBox() {
		dragStart = null;
		dragStop = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		if (dragStart == null) dragStart = e.getPoint();
		dragStop = e.getPoint();
		setNeedsRedraw();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased();
		if (!hasDragInput()) return;

		ensureDragPointOrder();
		handleDragSelection();
		unsetSelectionBox();
		setNeedsRedraw();
	}

	private void handleDragSelection() {
		List<Location> selected = new ArrayList<Location>();
		points.clear();
		for (Location each: map().locations()) {
			int x = (int) Math.round(each.x() * map().getWidth());
			int y = (int) Math.round(each.y() * map().getWidth());
			if (x < dragStop.x && x > dragStart.x && y < dragStop.y && y > dragStart.y) {
				selected.add(each);
				points.add(new Point(x, y));
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

	public void indicesSelected(int[] indices) {
		points.clear();
		List<Location> locations = new ArrayList<Location>();
		for (int index: indices) {
			Location location = map().locationAt(index);
			locations.add(location);
			points.add(new Point(location.px(), location.py()));
		}
		setNeedsRedraw();
	}

	protected void setNeedsRedraw() {
		needsRedraw = true;
	}

	protected boolean hasDragInput() {
		return dragStart != null && dragStop != null;
	}

	private Map map() {
		return viz == null ? null : viz.map;
	}

	private int width() {
		return height();
	}

	private int height() {
		return viz == null ? 0 : viz.map.getParameters().width;
	}

	public void updateSelection(List<String> handleIdentifiers) {
		points.clear();
		addSelection(handleIdentifiers);
	}

	public void setVisualization(MapVisualization viz) {
		if (viz == this.viz) return;
		this.points.clear();
		this.viz = viz;
		size(width(), height());

		int dimension = viz.map.getParameters().width;
		if (bg == null || dimension != this.bg.width) {
			bg = createGraphics(dimension, dimension, JAVA2D);
		}
		setupBackground();
		setNeedsRedraw();
		repaint();
	}

	public void addSelection(List<String> handleIdentifiers) {
		for (Location each: viz.map.locations()) {
			if (handleIdentifiers.contains(each.document().getIdentifier())) {
				points.add(new Point(each.px(), each.py()));
			}
		}
		setNeedsRedraw();
	}

}