package ch.deif.meander.ui;

import java.awt.Point;

import processing.core.PApplet;
import ch.deif.meander.visual.MapVisualization;

@SuppressWarnings("serial")
public class MeanderApplet extends PApplet {

	private MapVisualization viz;
	private Point dragStart;
	private Point dragStop;
	private CodemapEventRegistry events;

	public MeanderApplet() {
		events = new CodemapEventRegistry();
	}

	public void addListener(CodemapListener listener) {
		events.addListener(listener);
	}

	public void removeListener(CodemapListener listener) {
		events.removeListener(listener);
	}

	public void fireEvent(String key, Object value) {
		events.fireEvent(key, null, value); // TODO do we need the source field?
	}
	
	@Override
	public void setup() {
		frameRate(25);
	}

	@Override
	public void draw() {
		smooth();
		noFill();
		if (viz != null) viz.draw(g, this);
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
		repaint();
	}
	
	public MapVisualization getVisualization() {
		return viz;
	}

}