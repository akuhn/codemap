package ch.deif.meander.visual;

import java.awt.Point;
import java.awt.event.MouseEvent;

import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSetting;
import ch.deif.meander.ui.MeanderApplet.Events;

public abstract class Layer {

	public static final MapSetting<Boolean> GRAYSCALE = MapSetting.define("GRAYSCALE", false);
	
	public abstract void draw(MapInstance map, PGraphics pg);

	// TODO implement instead an handleEvent() as in DOM events.
	
	public void mouseDragStopped() { };

	public void mouseDraggedTo(Point dragStop) { };

	public void mouseDraggedStarted(Point dragStart) { };

	public void mouseClicked(MouseEvent e) { }

	public void registerEventHandlers(Events events) {
		// TODO Auto-generated method stub
		
	};
	
}
