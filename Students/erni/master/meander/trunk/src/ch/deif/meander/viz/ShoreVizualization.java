package ch.deif.meander.viz;

import processing.core.PGraphics;
import ch.deif.meander.Colors;
import ch.deif.meander.Map;
import ch.deif.meander.Map.Pixel;

public class ShoreVizualization extends MapVisualization {

	private final Colors color;
	private float[][] alsoShow;

	public ShoreVizualization(Map map) {
		super(map);
		map.needElevationModel();
		color = map.getParameters().blackAndWhite ? new Colors(204,204,204) : Colors.SHORE;
	}

	@Override
	public void draw(PGraphics pg) {
		pg.loadPixels();
		int[] pixels = pg.pixels;
		int index = 0;
		for (Pixel each: getMap().pixels()) {
			if (each.elevation() > getMap().getParameters().waterHeight) 
				pixels[index] = color.asRGB();
			if (alsoShow != null && alsoShow[each.px][each.py] > getMap().getParameters().waterHeight)
				pixels[index] = color.asRGB();
			index++;
		}
		pg.updatePixels();
	}

	public void alsoShowShoresOf(float[][] DEM) {
		this.alsoShow = DEM;
	}

}
