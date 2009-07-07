package ch.deif.meander.visual;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class ArrowOverlay extends Layer {

	@Override
	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		draw(map, pg, pa);
	}

	@Override
	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		new Helper(map).draw(pg);
	}

	static class Helper {
	
		private MapInstance map;
	
		public Helper(MapInstance map2) {
			this.map = map2;
		}
		
		private Composite<Arrow> arrows = Composite.newInstance();
		
		public void arrow(Location from, Location to, double d) {
			arrows.append(new Arrow(from, to, (float) d));
		}
		
		public void draw(PGraphics pg) {
			arrows.draw(null, pg, null);
		}
		
	}
	
	static class Arrow extends Layer {

	public Arrow(Location from, Location to, float weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		draw(map, pg, pa);
	}

	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		int x1 = from.px;
		int y1 = from.py;
		int x2 = to.px;
		int y2 = to.py;
		float w = Math.max(4.0f, weight);
		pg.noFill();
		pg.stroke(0, 0, 0, 20);
		drawArrowShape(pg, x1 + 3, y1 + 3, x2 + 3, y2 + 3, w, w - 2);
		drawArrowShape(pg, x1 + 3, y1 + 3, x2 + 3, y2 + 3, w, w);
		drawArrowShape(pg, x1 + 3, y1 + 3, x2 + 3, y2 + 3, w, w + 2);
		pg.stroke(255);
		pg.strokeWeight(w);
		drawArrowShape(pg, x1, y1, x2, y2, w, w);
		pg.strokeWeight(1.0f);
	}

	private void drawArrowShape(PGraphics pg, float x1, float y1, float x2, float y2, float len, float w) {
		pg.strokeWeight(w);
		pg.line(x1, y1, x2, y2);
		pg.pushMatrix();
		pg.translate(x2, y2);
		float a = (float) Math.atan2(x1 - x2, y2 - y1);
		pg.rotate(a);
		pg.beginShape();
		pg.vertex(len * -1.5f, len * -2.5f);
		pg.vertex(0, 0);
		pg.vertex(len * +1.5f, len * -2.5f);
		pg.endShape();
		pg.popMatrix();
	}

	public Location from, to;
	public float weight;
}
	
}
