package ch.deif.meander;

import java.awt.Color;

import processing.core.PImage;
import ch.deif.meander.Map.Pixel;

@SuppressWarnings("serial")
public class HillshadeVisualization extends MapVisualization {

    public HillshadeVisualization(Map map) {
        super(map);
    }

    @Override
    public void draw() {
        PImage img = new PImage(map.width, map.height);
        int[] pixels = img.pixels;
        int index = 0;
        for (Pixel p: map.pixels()) {
            MColor color = color(p);
            color.darker(p.hillshade());
            if (p.hasContourLine()) color.darker();
            pixels[index++] = color.rgb();
        }
        //img.updatePixels();
        //triangle(0, 0, 30, 30, 30, 0);
        //line(20, 20, 50, 50);
        image(img, 0, 0);
    }

    private MColor color(Pixel p) {
    	Parameters params = map.getParameters();
    	double elevation = p.elevation();
    	if (elevation > params.beachHeight) return MColor.GREEN();
    	if (elevation > params.waterHeight) return MColor.YELLOW();
    	return MColor.BLUE();
    }
}
