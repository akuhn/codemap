package ch.deif.meander.visual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.MapInstance;

public class Composite<E extends Layer> extends Layer implements Iterable<E> {

	private List<E> children;
	
	public Composite() {
		this.children = new ArrayList<E>();
	}
	
	public void drawFigure(MapInstance map, PGraphics pg) {
		// by default, do nothing
	}

	public final void draw(MapInstance map, PGraphics pg) {
		draw(map, pg, null);
	}

	public final void draw(MapInstance map, PGraphics pg, PApplet pa) {
		this.drawFigure(map, pg);
		this.drawChildren(map, pg);
	}

	public void drawChildren(MapInstance map, PGraphics pg) {
		for (Layer each: children) {
			each.draw(map, pg, null);
		}
	}

	public static final <T extends Layer> Composite<T> newInstance() {
		return new Composite<T>();
	}

	public static final <T extends Layer> Composite<T> of(T... elements) {
		Composite<T> composite = new Composite<T>();
		for (T each: elements) composite.append(each);
		return composite;
	}

	public void append(E element) {
		children.add(element);
	}

	@Override
	public Iterator<E> iterator() {
		return children.iterator();
	}
	
}
