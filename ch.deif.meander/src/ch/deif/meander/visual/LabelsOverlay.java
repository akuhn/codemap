package ch.deif.meander.visual;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import processing.core.PFont;
import processing.core.PGraphics;
import ch.akuhn.util.Get;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.util.MapScheme;

public class LabelsOverlay extends Layer {

	private final MapScheme<String> labelScheme;

	public LabelsOverlay(MapScheme<String> labelScheme) {
		this.labelScheme = labelScheme;
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		new Helper(map, labelScheme).draw(map, pg);
	}

	private static class Helper {
		
		private PFont PFONT = new PFont(PFont.findFont("Arial Narrow"), true, PFont.DEFAULT_CHARSET);
		private boolean layoutDone = false;
		private Composite<Label> labels = Composite.newInstance();
	
		public Helper(MapInstance map, MapScheme<String> labelScheme) {
			double max = maxElevation(map);
			for (Location each: map.locations()) {
				Label l = new Label(labelScheme.forLocation(each.getPoint()));
				labels.append(l);
				l.x = l.x0 = each.px;
				l.y = l.y0 = each.py;
				l.size = (float) (each.getElevation() / max * map.getWidth() / 24);
			}
		}

		private double maxElevation(MapInstance map) {
			double max = Double.MIN_VALUE;
			for (Location each: map.locations()) {
				max = Math.max(max, each.getElevation());
			}
			return max;
		}

		public void draw(MapInstance map, PGraphics pg) {
			pg.textFont(PFONT);
			this.layout(pg);
			pg.fill(255, 0, 0);
			pg.textSize(20);
			labels.draw(map, pg, null);
		}

		private void layout(PGraphics pg) {
			if (layoutDone) return;
			for (Label each: labels)
				each.initializeWidth(pg);
			List<Label> done = new ArrayList<Label>();
			for (Label each: Get.sorted(labels)) {
				layoutLoopBody(done, each);
			}
			layoutDone = true;
		}

		private void layoutLoopBody(List<Label> done, Label each) {
			for (; each.hasNextPosition(); each.nextPosition()) {
				Rectangle bounds = each.getBounds();
				if (intersectsAnyDone(done, bounds)) continue;
				done.add(each);
				return;
			}
			each.hidden = true;
		}

		private boolean intersectsAnyDone(List<Label> done, Rectangle bounds) {
			for (Label eachDone: done) {
				if (bounds.intersects(eachDone.getBounds())) return true;
			}
			return false;
		}

	}

	private static class Label extends Layer implements Comparable<Label> {

	// TODO extract parts of this class into a figure superclass, eg positioning and bounding box

	public static boolean DRAFT = false;

	private String text;
	public float size = 12;
	public float x0, y0, x, y;
	public float width;
	private Position pos = Position.TOPLEFT;
	boolean hidden;

	public Label(String text) {
		assert text != null;
		this.text = text;
	}

	public void initializeWidth(PGraphics pg) {
		pg.textSize(size);
		width = pg.textWidth(text);
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		if (DRAFT) {
			pg.stroke(0);
			pg.noFill();
			pg.rect(x, y, width, -(size * 0.6f));
			pg.ellipse(x0, y0, 5, 5);
		}
		if (hidden) return;
		pg.textSize(size);
		pg.fill(0,0,0,64);
		pg.text(text, x + 1, y + 1);
		pg.fill(255);
		pg.text(text, x, y);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) (y - (size)), (int) width, (int) (size));
	}

	@Override
	public int compareTo(Label other) {
		boolean thisTest = this.text.endsWith("Test");
		boolean otherTest = other.text.endsWith("Test");
		if (thisTest != otherTest) return thisTest ? 1 : -1;
		return (int) (other.size - this.size);
	}

	public boolean hasNextPosition() {
		return pos.ordinal() < 3;
	}

	public void nextPosition() {
		pos = Position.values()[pos.ordinal() + 1];
		y = y0 + ((pos == Position.BOTTOMLEFT || pos == Position.BOTTOMRIGHT) ? (size * 0.6f) : 0);
		x = x0 - ((pos == Position.BOTTOMLEFT || pos == Position.TOPLEFT) ? width : 0);
	}

	private enum Position {

		TOPRIGHT, TOPLEFT, BOTTOMLEFT, BOTTOMRIGHT;

	}
	}
}
