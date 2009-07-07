package ch.deif.meander.visual;

import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSetting;
import ch.deif.meander.ui.MeanderApplet;

public abstract class Layer {

	public static final MapSetting<Boolean> GRAYSCALE = MapSetting.define("GRAYSCALE", false);
	
	public abstract void draw(MapInstance map, PGraphics pg, MeanderApplet pa);
	
}
