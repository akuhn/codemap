package ch.deif.meander;

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
            double color = 255.0 * p.hillshade();
            pixels[index++] = 
                p.elevation() > 10 ?
                ((int) Math.max(0, Math.min(color * (p.hasContourLine() ? 0.5 : 1), 255)) << 8) :
                (int) Math.max(0, Math.min(color, 255));
        }
        img.updatePixels();
        image(img, 0, 0);    
    }

}
