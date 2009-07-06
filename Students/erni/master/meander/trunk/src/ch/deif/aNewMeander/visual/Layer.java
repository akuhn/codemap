package ch.deif.aNewMeander.visual;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapConfigurationWithSize;

public interface Layer {

	public abstract void draw(MapConfigurationWithSize map, PGraphics pg);
	
}
