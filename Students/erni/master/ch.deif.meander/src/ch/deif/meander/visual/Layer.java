package ch.deif.meander.visual;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSetting;

public abstract class Layer {

	public static final MapSetting<Boolean> GRAYSCALE = MapSetting.define("GRAYSCALE", false);
	
	public abstract void draw(MapInstance map, PGraphics pg);

	public abstract void draw(MapInstance map, PGraphics pg, PApplet pa);
	
}
