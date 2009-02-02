package ch.deif.meander;

import processing.core.PImage;
import ch.deif.meander.Map.Kernel;

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
        double zenithRad = 45 * PI / 180;
        double azimuthRad = (315 - 180) * Math.PI / 180;
        double z_factor = 0.3;
        for (Kernel k: map.kernels()) {
            
            double dx = (k.c + (2 * k.f) + k.i - (k.a + (2 * k.d) + k.g)) / 8 * 1;
            double dy = (k.g + (2 * k.h) + k.i - (k.a + (2 * k.b) + k.c)) / 8 * 1;
            double slopeRad = Math.atan(z_factor * Math.sqrt(dx * dx + (dy * dy)));
            double aspectRad = Math.atan2(dy, -dx);
            if (aspectRad < 0) { aspectRad = 2 * Math.PI + aspectRad; };
            double color = (255.0 * (Math.cos(zenithRad) * Math.cos(slopeRad) 
                      + (Math.sin(zenithRad) * Math.sin(slopeRad) * Math.cos(azimuthRad - aspectRad))));
            pixels[index++] = (int) Math.max(0, Math.min(color, 255));
        }
        img.updatePixels();
        image(img, 0, 0);    }

}
