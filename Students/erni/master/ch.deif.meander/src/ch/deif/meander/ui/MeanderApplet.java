package ch.deif.meander.ui;

import processing.core.PApplet;
import ch.deif.meander.visual.MapVisualization;

@SuppressWarnings("serial")
public class MeanderApplet extends PApplet {

	private MapVisualization viz;
	private CodemapEventRegistry events;

	public MeanderApplet() {
		events = new CodemapEventRegistry();
	}	

	@Override
	public synchronized void redraw() {
		super.redraw();
		// we might be called during initialization when we do not have a visualization yet.
		if (viz == null) return;
		viz.redraw();
	}

	public void addListener(CodemapListener listener) {
		events.addListener(listener);
	}

	public void removeListener(CodemapListener listener) {
		events.removeListener(listener);
	}

	public void fireEvent(String key, Object source, Object value) {
		events.fireEvent(key, source, value);
	}
	
	@Override
	public void setup() {
		frameRate(25);
	}

	@Override
	public void draw() {
		smooth();
		noFill();
		try {
			if (viz != null) viz.draw(g, this);						
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void setVisualization(MapVisualization viz) {
		if (viz == this.viz) return;
		// Processing requires that use use #size instead of #setSize! 
		size(viz.getWidth(), viz.getHeight());
		this.viz = viz;
	}
	
	public MapVisualization getVisualization() {
		return viz;
	}

}