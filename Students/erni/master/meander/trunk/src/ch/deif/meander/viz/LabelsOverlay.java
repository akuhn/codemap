package ch.deif.meander.viz;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import processing.core.PFont;
import processing.core.PGraphics;
import ch.akuhn.util.Get;
import ch.deif.meander.Location;
import ch.deif.meander.Map;

public class LabelsOverlay extends MapVisualization<Label> {

	private PFont PFONT = new PFont(PFont.findFont("Arial Narrow"), true, PFont.DEFAULT_CHARSET);
	private boolean layoutDone = false;

	public LabelsOverlay(Map map) {
		super(map);
		for (Location each: map.locations()) {
			Label l = new Label(each.getName());
			addChild(l);
			l.x = l.x0 = each.px();
			l.y = l.y0 = each.py();
			l.size = (float) each.normElevation() / 4f;
		}
	}

	@Override
	public void drawThis(PGraphics pg) {
		pg.textFont(PFONT);
		this.layout(pg);
		pg.fill(255, 0, 0);
		pg.textSize(20);

	}

	private void layout(PGraphics pg) {
		if (layoutDone) return;
		for (Label each: children())
			each.initializeWidth(pg);
		List<Label> done = new ArrayList<Label>();
		for (Label each: Get.sorted(children())) {
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
