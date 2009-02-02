package ch.deif.meander;

import static java.lang.Math.max;

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
            Color color = color(p);
            color = shade(color, p.hillshade());
            color = contour(color, p.hasContourLine());
            pixels[index++] = color.getRGB();
        }
        img.updatePixels();
        image(img, 0, 0);    
    }

    private Color contour(Color color, boolean hasContourLine) {
        return hasContourLine ? color.darker() : color;
    }

    private Color color(Pixel p) {
        return p.elevation() > 10 ? Color.GREEN : Color.BLUE ;
    }
    
    /**
     * 
     * @param hillshade a shading value between 0.0 and 1.0 
     * (caution: may exceed this range by a small amount). 
     */
    private Color shade(Color color, double hillshade) {
        // TODO can we avoid using Color?
        return new Color(
                max((int)(color.getRed() * hillshade), 0), 
                max((int)(color.getGreen() * hillshade), 0),
                max((int)(color.getBlue() * hillshade), 0));
    }
    
}
