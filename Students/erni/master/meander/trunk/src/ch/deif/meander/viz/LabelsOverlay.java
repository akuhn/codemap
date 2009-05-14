package ch.deif.meander.viz;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import processing.core.PFont;
import processing.core.PGraphics;
import ch.akuhn.util.Get;
import ch.deif.meander.Location;
import ch.deif.meander.Map;

public class LabelsOverlay extends MapVisualization {

	private PFont PFONT = new PFont(PFont.findFont("Arial Narrow"), true, PFont.DEFAULT_CHARSET);
	private boolean layoutDone = false;
	private Composite<Label> labels = Composite.newInstance();
	
	public LabelsOverlay(Map map) {
		super(map);
		double max = maxElevation(map);
		for (Location each: map.locations()) {
			Label l = new Label(each.name());
			labels.add(l);
			l.x = l.x0 = each.px();
			l.y = l.y0 = each.py();
			l.size = (float) (each.elevation() / max * map.getWidth() / 24);
		}
	}

	private double maxElevation(Map map) {
		double max = Double.MIN_VALUE;
		for (Location each: map.locations()) {
			max = Math.max(max, each.elevation());
		}
		return max;
	}

	@Override
	public void draw(PGraphics pg) {
		pg.textFont(PFONT);
		this.layout(pg);
		pg.fill(255, 0, 0);
		pg.textSize(20);
		labels.draw(pg);
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
