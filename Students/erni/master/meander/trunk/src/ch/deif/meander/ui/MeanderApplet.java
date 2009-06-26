package ch.deif.meander.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.viz.MapVisualization;

@SuppressWarnings("serial")
public class MeanderApplet extends PApplet {

	private MapVisualization viz;

	private boolean needsRedraw;
	private PGraphics bg;

	private Point dragStart;
	private Point dragStop;

	private Events events;

	public static class Events {

		private List<MeanderEventListener> listeners;
		private MeanderApplet applet;

		private Events(MeanderApplet meanderApplet) {
			this.listeners = new ArrayList<MeanderEventListener>();
			this.applet = meanderApplet;
		}

		public void addListener(MeanderEventListener listener) {
			this.listeners.add(listener);
		}

		public void removeListener(MeanderEventListener listener) {
			this.listeners.remove(listener);
		}

		public void doubleClicked(final Location location) {
			// TODO comment WHY we use a thread here
			if (listeners != null) new Thread() {
				@Override
				public void run() {
					for (MeanderEventListener each: listeners) {
						each.doubleClicked(location);
					}
				}
			}.start();
		}

		public void selectionChanged(final Location... locations) {
			// TODO comment WHY we use a thread here
			if (listeners != null) new Thread() {
				@Override
				public void run() {
					for (MeanderEventListener each: listeners) {
						each.selectionChanged(locations);
					}					
				}
			}.start();
		}

	}

	public MeanderApplet() {
		events = new Events(this);
	}

	protected Events events() {
		return events;
	}

	public void addListener(MeanderEventListener listener) {
		events().addListener(listener);
	}

	public void removeListener(MeanderEventListener listener) {
		events().removeListener(listener);
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
//		strokeWeight(POINT_STROKE);
		drawBackground();
		viz.draw(g);
		needsRedraw = false;
	}

	private void drawBackground() {
		image(bg, 0, 0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		viz.mouseClicked(e);
		setNeedsRedraw();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		if (dragStart == null) {
			dragStart = e.getPoint();
			viz.mouseDragStarted(dragStart);
		}
		dragStop = e.getPoint();
		viz.mouseDraggedTo(dragStop);
		setNeedsRedraw();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased();
		if (!hasDragInput()) return;
		viz.mouseDragStopped();
		dragStart = null;
		dragStop = null;
		setNeedsRedraw();
	}

	public void indicesSelected(int[] indices) {
		viz.indicesSelected(indices);
		setNeedsRedraw();
	}

	protected void setNeedsRedraw() {
		needsRedraw = true;
	}

	public boolean hasDragInput() {
		return dragStart != null && dragStop != null;
	}

	private int width() {
		return height();
	}

	private int height() {
		return viz == null ? 0 : viz.map.getParameters().width;
	}

	public void updateSelection(List<String> handleIdentifiers) {
		viz.updateSelection(handleIdentifiers);
		setNeedsRedraw();
	}

	public void setVisualization(MapVisualization viz) {
		if (viz == this.viz) return;
		this.viz = viz;
		this.viz.registerEventHandler(events);
		size(width(), height());
		
		// TODO somehow clean up the parameters ... maybe singleton 
		int dimension = viz.map.getParameters().width;
		if (bg == null || dimension != this.bg.width) {
			bg = createGraphics(dimension, dimension, JAVA2D);
		}
		setupBackground();
		setNeedsRedraw();
		repaint();
	}
	
	public MapVisualization getVisualization() {
		return viz;
	}

	public void addSelection(List<String> handleIdentifiers) {
		viz.addSelection(handleIdentifiers);
		setNeedsRedraw();
	}

}