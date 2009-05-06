package ch.deif.meander.viz;

import java.util.ArrayList;

import processing.core.PGraphics;


@SuppressWarnings("serial")
public class Composite<E extends Drawable> extends ArrayList<E> implements Drawable {

	public void drawFigure(PGraphics pg) {
		// by default, do nothing
	}

	public final void draw(PGraphics pg) {
		this.drawFigure(pg);
		this.drawChildren(pg);
	}

	public void drawChildren(PGraphics pg) {
		for (Drawable each: this) {
			each.draw(pg);
		}
	}

	public static final <T extends Drawable> Composite<T> newInstance() {
		return new Composite<T>();
	}

	public static final <T extends Drawable> Drawable of(T... elements) {
		Composite<T> composite = new Composite<T>();
		for (T each: elements) composite.add(each);
		return composite;
	}

}
