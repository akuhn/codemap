package ch.deif.meander;

import processing.core.PGraphics;
import processing.core.PImage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SketchVisualization extends MapVisualization {

    // TODO make sure circle size has same diameter as coastline of shaded hills

    public SketchVisualization(Map map) {
        super(map);
    }

    @Override
    public void draw(PGraphics pg) {
        float width = map.getParameters().width;
        float height = map.getParameters().height;
        pg.background(204);
        pg.stroke(0);
        pg.noFill();
        pg.smooth();
        for (Location each : map.locations()) {
            float x = (float) (each.x * width);
            float y = (float) (each.y * height);
            float r = (float) (each.height / 100 * width / 2.61f);
            pg.ellipse(x, y, r, r);
        }
    }

    @Override
    public void drawOn(PImage img) {
        throw new NotImplementedException();
    }

}
