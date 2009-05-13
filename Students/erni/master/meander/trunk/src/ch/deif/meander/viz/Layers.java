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

	public Layers add(Drawable overlay) {
		layers.add(overlay);
		return this;
	}

	@Override
	public void draw(PGraphics pg) {
		assert pg != null;
		background.draw(pg);
		for (Drawable each: layers) each.draw(pg);
	}

	public Layers useHillshading() {
		background = Composite.of(
				new WaterVisualization(map),
				new ShoreVizualization(map),
				new HillshadeVisualization(map),
				new LabelsOverlay(map));
		return this;
	}

	public Layers add(Class<? extends MapVisualization> overlay) {
		try {
			add(overlay.getConstructor(Map.class).newInstance(map));
			return this;
		} catch (Exception ex) {
			throw Throw.exception(ex);
		}
	}

}
