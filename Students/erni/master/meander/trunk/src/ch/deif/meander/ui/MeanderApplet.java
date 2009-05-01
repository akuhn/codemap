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

	private MapVisualization<?> viz;
	private Collection<Point> points;

	private boolean needsRedraw;
	private PGraphics bg;

	private Point dragStart;
	private Point dragStop;

	private MeanderEventListener listener;

	public static final int PIXELSCALE = 512;

	public MeanderApplet() {
		this(null);
	}

	public MeanderApplet(MapVisualization<?> vizualization) {
		points = Collections.synchronizedSet(new HashSet<Point>());
		bg = createGraphics(width(), height(), JAVA2D);
		setVisualization(vizualization);
	}

	public void addListener(MeanderEventListener listener) {
		assert this.listener == null;
		this.listener = listener;
	}

	public void removeListener(MeanderEventListener listener) {
		assert this.listener == listener;
		this.listener = null;
	}

	@Override
	public void setup() {
		size(width(), height());
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
		if (dragStart != null && dragStop != null) {
			stroke(Color.RED.getRGB());
			strokeWeight(BOX_STROKE);
			int deltaX = dragStop.x - dragStart.x;
			int deltaY = dragStop.y - dragStart.y;
			rect(dragStart.x, dragStart.y, deltaX, deltaY);
			strokeWeight(POINT_STROKE);
		}
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
			// button1 is 1st mouse button
			NearestNeighbor nn = new MaxDistNearestNeighbor(map(), width() / 10);
			Point nearest = nn.forLocation(point);
			if (nearest != null) {
				addSelection(nearest);
				doubleClicked(nn.location());
			}			
		} else  if (e.getButton() == MouseEvent.BUTTON1) {
			// button1 is 1st mouse button
			NearestNeighbor nn = new MaxDistNearestNeighbor(map(), width() / 10);
			Point nearest = nn.forLocation(point);
			if (nearest != null) {
				addSelection(nearest);
				selectionChanged(nn.location());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// button3 is 2nd mouse button
			NearestNeighbor nn = new NearestNeighbor(map());
			Point nearest = nn.forLocation(point);
			addSelection(nearest);
			selectionChanged(nn.location());
		}
		unsetSelectionBox();
		needsRedraw();
	}

	private void doubleClicked(final Location location) {
		if (listener != null) new Thread() {
			@Override
			public void run() {
				listener.doubleClicked(location.getDocument().name());
			}
		}.start();

		
	}

	private void selectionChanged(final Location... locations) {
		final ArrayList<String> names = new ArrayList<String>();
		for (Location each: locations)
			names.add(each.getDocument().name());
		if (listener != null) new Thread() {
			@Override
			public void run() {
				listener.selectionChanged(names.toArray(new String[0]));
			}
		}.start();

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
		needsRedraw();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased();
		if (dragStart != null && dragStop != null) {
			// make sure that start is top-left and stop is bottom-right
			int minX = Math.min(dragStart.x, dragStop.x);
			int minY = Math.min(dragStart.y, dragStop.y);
			int maxX = Math.max(dragStart.x, dragStop.x);
			int maxY = Math.max(dragStart.y, dragStop.y);
			dragStart = new Point(minX, minY);
			dragStop = new Point(maxX, maxY);
			List<Location> selected = new ArrayList<Location>();
			points.clear();
			for (Location each: map().locations()) {
				int x = (int) Math.round(each.x() * map().getHeight());
				int y = (int) Math.round(each.y() * map().getHeight());
				if (x < dragStop.x && x > dragStart.x && y < dragStop.y && y > dragStart.y) {
					selected.add(each);
					points.add(new Point(x, y));
				}
			}
			selectionChanged(selected.toArray(new Location[0]));
			unsetSelectionBox();
			needsRedraw();
		}
	}

	public void indicesSelected(int[] indices) {
		points.clear();
		List<Location> locations = new ArrayList<Location>();
		for (int index: indices) {
			Location location = map().locationAt(index);
			locations.add(location);
			int x = (int) Math.round(location.x() * map().getHeight());
			int y = (int) Math.round(location.y() * map().getHeight());
			points.add(new Point(x, y));
		}
		needsRedraw();
	}

	protected void needsRedraw() {
		needsRedraw = true;
	}

	private Map map() {
		return viz == null ? null : viz.map;
	}

	private int width() {
		return MeanderApplet.PIXELSCALE;
	}

	private int height() {
		return MeanderApplet.PIXELSCALE;
	}

	public void updateSelection(List<String> handleIdentifiers) {
		points.clear();
		for (Location each: viz.map.locations())
			if (handleIdentifiers.contains(each.getDocument().name())) {
				int x = (int) Math.round(each.x() * map().getHeight());
				int y = (int) Math.round(each.y() * map().getHeight());
				points.add(new Point(x, y));
			}
		needsRedraw();
	}

	public void setVisualization(MapVisualization<?> viz) {
		if (viz == this.viz) return;
		this.points.clear();
		this.viz = viz;
		setupBackground();
		needsRedraw();
		repaint();
	}

}
