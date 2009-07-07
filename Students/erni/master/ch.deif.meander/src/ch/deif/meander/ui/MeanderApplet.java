package ch.deif.meander.ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import ch.deif.meander.Location;
import ch.deif.meander.visual.MapVisualization;

@SuppressWarnings("serial")
public class MeanderApplet extends PApplet {

	private MapVisualization viz;

	private boolean needsRedraw;

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

	@Override
	public void draw() {
		if (!needsRedraw) return;

		smooth();
		noFill();
//		strokeWeight(POINT_STROKE);
		viz.draw(g, this);
		needsRedraw = false;
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
		return viz == null ? 0 : viz.getMap().height;
	}

	public void setVisualization(MapVisualization viz) {
		if (viz == this.viz) return;
		this.viz = viz;
		this.viz.registerEventHandler(events);
		size(width(), height());
		
		setNeedsRedraw();
		repaint();
	}
	
	public MapVisualization getVisualization() {
		return viz;
	}

}