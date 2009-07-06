package ch.deif.aNewMeander.visual;

import java.util.ArrayList;

import processing.core.PGraphics;
import ch.deif.aNewMeander.MapConfigurationWithSize;


@SuppressWarnings("serial")
public class Composite<E extends Layer> extends ArrayList<E> implements Layer {

	public void drawFigure(MapConfigurationWithSize map, PGraphics pg) {
		// by default, do nothing
	}

	public final void draw(MapConfigurationWithSize map, PGraphics pg) {
		this.drawFigure(map, pg);
		this.drawChildren(map, pg);
	}

	public void drawChildren(MapConfigurationWithSize map, PGraphics pg) {
		for (Layer each: this) {
			each.draw(map, pg);
		}
	}

	public static final <T extends Layer> Composite<T> newInstance() {
		return new Composite<T>();
	}

	public static final <T extends Layer> Composite<T> of(T... elements) {
		Composite<T> composite = new Composite<T>();
		for (T each: elements) composite.add(each);
		return composite;
	}

}
