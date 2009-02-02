package ch.deif.meander;

import processing.core.PImage;
import ch.deif.meander.Map.Pixel;

@SuppressWarnings("serial")
public class DebugDEMVisualization extends MapVisualization {

    public DebugDEMVisualization(Map map) {
        super(map);
    }

    @Override
    public void draw() {
        PImage img = new PImage(map.width, map.height);
        int[] pixels = img.pixels;
        int index = 0;
        for (Pixel p: map.pixels()) {
            pixels[index++] = (int) Math.min(p.elevation(), 255);
        }
        img.updatePixels();
        image(img, 0, 0);
    }

}
