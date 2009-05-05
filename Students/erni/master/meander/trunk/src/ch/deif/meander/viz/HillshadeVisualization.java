package ch.deif.meander.viz;

import processing.core.PGraphics;
import processing.core.PImage;
import ch.deif.meander.Colors;
import ch.deif.meander.Map;
import ch.deif.meander.Parameters;
import ch.deif.meander.Map.Pixel;

public class HillshadeVisualization extends MapVisualization<Drawable> {

	public HillshadeVisualization(Map map) {
		super(map);
	}

	@Override
	public void drawThis(PGraphics pg) {
		PImage img = new PImage(map.getWidth(), map.getHeight());
		this.drawOn(img);
		pg.image(img, 0, 0);
	}

	private Colors color(Pixel p) {
		Parameters params = map.getParameters();
		double elevation = p.elevation();
		if (elevation > params.beachHeight) return p.nearestNeighborColor();
		if (elevation > params.waterHeight) return Colors.SHORE;
		return Colors.WATER;
	}

	private void drawOn(PImage img) {
		assert img.width == map.getWidth() && img.height == map.getHeight();
		int[] pixels = img.pixels;
		int index = 0;
		for (Pixel p: map.pixels()) {
			double shading = 1.0;
			if (p.elevation() > map.getParameters().beachHeight) {
				shading = p.hillshade();
				if (p.hasContourLine()) shading *= 0.5;
			}
			pixels[index++] = color(p).scaledRGB(shading);
		}
		img.updatePixels();
	}
}
