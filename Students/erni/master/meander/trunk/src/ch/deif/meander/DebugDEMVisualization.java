package ch.deif.meander;

import processing.core.PGraphics;
import processing.core.PImage;
import ch.deif.meander.Map.Pixel;

public class DebugDEMVisualization extends MapVisualization {

    public DebugDEMVisualization(Map map) {
        super(map);
    }

    @Override
    public void draw(PGraphics pg) {
        PImage img = new PImage(map.width, map.height);
        this.drawOn(img);
        pg.image(img, 0, 0);
    }

    @Override
    public void drawOn(PImage img) {
        int[] pixels = img.pixels;
        int index = 0;
        for (Pixel p : map.pixels()) {
            pixels[index++] = (int) Math.min(p.elevation(), 255);
        }
        img.updatePixels();
    }

}
