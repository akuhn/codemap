package ch.deif.meander;

import processing.core.PApplet;

public class Map {

    private Pixel[][] array;
    private MapDescription description;    

    public Map(MapDescription description) {
        this.description = description;
        array = new Pixel[description.getParameters().width][description.getParameters().height];
    }

    public void drawOn(PApplet g) {
        float width = description.getParameters().width;
        float height = description.getParameters().height;
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

    public int getWidth() {
        return description.getParameters().width;
    }

    public int getHeight() {
        return description.getParameters().height;
    }
    
}
