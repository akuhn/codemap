package ch.deif.meander;

import processing.core.PImage;

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
            int gray = (int) Math.min(p.elevation() * 2, 255);
            pixels[index++] = gray + gray << 8 + gray << 16;
        }
        image(img, 0, 0);
    }

}
