package ch.deif.meander.viz;

import processing.core.PGraphics;

public class Label implements Drawable {

    private String text;
    float size = 12;
    public float x, y;
    
    public Label(String text) {
        this.text = text;
    }
    
    public float getWidth(PGraphics pg) {
        pg.textSize(size);
        return pg.textWidth(text);
    }
    
    public float getHeight(PGraphics pg) {
        return size;
    }
    
    @Override
    public void draw(PGraphics pg) {
        pg.stroke(0);
        pg.fill(204);
        pg.rect(x, y, getWidth(pg), -size);
        pg.ellipse(x, y, 5, 5);
        pg.fill(0);
        pg.textSize(size);
        pg.text(text, x, y);
    }

}
