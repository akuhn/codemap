package sandbox;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;

public class SpikeMap extends PApplet {

    private static final long serialVersionUID = 1L;
    private PGraphics pg;

    @Override
    public void draw() {
        image(pg, 0, 0);
    }

    @Override
    public void setup() {
        size(512, 512);
        pg = createGraphics(512, 512, JAVA2D);
        pg.beginDraw();
        pg.ellipse(200, 200, 100, 100);
        pg.loadPixels();
        for (int n = 0; n < 20000; n++) {
            pg.pixels[n] = color(0);
        }
        pg.updatePixels();
        pg.endDraw();
    }
    
    
    

}
