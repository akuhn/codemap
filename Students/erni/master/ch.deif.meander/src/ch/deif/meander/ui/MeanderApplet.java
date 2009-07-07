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