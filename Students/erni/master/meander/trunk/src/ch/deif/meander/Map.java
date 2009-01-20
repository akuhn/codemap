package ch.deif.meander;

import processing.core.PApplet;

public class Map {

    @SuppressWarnings("unused")
    private Pixel[][] array;
    private MapDescription description;    

    public Map(MapDescription description) {
        this.description = description;
        array = new Pixel[description.getParameters().width][description.getParameters().height];
    }

    public void drawOn(PApplet g) {
        float width = getParameters().width;
        float height = getParameters().height;
        g.background(204);
        g.stroke(0);
        g.noFill();
        for (Location each: description.locations()) {
            float x = (each.x + 1) * width / 2;
            float y = (1 - each.y) * height / 2;
            float r = each.height / 100 * width / 2.61f;
            g.ellipse(x, y, r, r);
        }
    }

    public Parameters getParameters() {
        return description.getParameters();
    }

    public Iterable<Location> locations() {
        return description.locations();
    }

    public MapVisualization createVisualization() {
        return new SketchVisualization(this);
    }

}
