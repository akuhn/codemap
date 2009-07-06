package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.MapInstance;

public interface Layer {

	public abstract void draw(MapInstance map, PGraphics pg);
	
}
