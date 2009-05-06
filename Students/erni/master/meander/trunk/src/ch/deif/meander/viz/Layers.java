package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.akuhn.util.Throw;
import ch.deif.meander.Map;

public class Layers extends MapVisualization {

	private Drawable background;
	private Composite<Drawable> layers = Composite.newInstance();

	public Layers(Map map) {
		super(map);
		useSketchBackground();
	}

	public void useSketchBackground() {
		background = new SketchVisualization(map);
	}

	public void add(Drawable overlay) {
		layers.add(overlay);
	}

	@Override
	public void draw(PGraphics pg) {
		background.draw(pg);
		for (Drawable each: layers) each.draw(pg);
	}

	public Layers useHillshading() {
		background = Composite.of(
				new WaterVisualization(map),
				new ShoreVizualization(map),
				new HillshadeVisualization(map));
		return this;
	}

	public void add(Class<? extends MapVisualization> overlay) {
		try {
			add(overlay.getConstructor(Map.class).newInstance(map));
		} catch (Exception ex) {
			throw Throw.exception(ex);
		}
	}

}
