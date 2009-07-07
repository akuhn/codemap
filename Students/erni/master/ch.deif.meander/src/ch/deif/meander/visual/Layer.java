package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSetting;

public interface Layer {

	public static final MapSetting<Boolean> GRAYSCALE = MapSetting.define("GRAYSCALE", false);
	
	public abstract void draw(MapInstance map, PGraphics pg);
	
}
