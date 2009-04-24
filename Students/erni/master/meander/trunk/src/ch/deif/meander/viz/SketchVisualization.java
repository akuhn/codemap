package ch.deif.meander.viz;

import ch.deif.meander.Location;
import ch.deif.meander.Map;
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
            float x = (float) (each.x() * width);
            float y = (float) (each.y() * height);
            float r = (float) (each.elevation() / 500 * width);
            pg.ellipse(x, y, r, r);
        }
    }

}
