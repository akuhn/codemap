package ch.deif.meander;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

public class ArrowOverlay extends MapVisualization {

    private List<Arrow> arrows = new ArrayList<Arrow>(); 
    
    public ArrowOverlay(Map map) {
        super(map);
    }

    @Override
    public void draw(PGraphics pg) {
        for (Arrow each: arrows) each.draw(pg);
    }

    public void arrow(Location from, Location to, float d) {
        arrows.add(new Arrow(from,to,d));
        
    }

    public class Arrow {
        
        public Arrow(Location from, Location to, float weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public void draw(PGraphics pg) {
            float x1 = from.x * pg.width;
            float y1 = from.y * pg.height;
            float x2 = to.x * pg.width;
            float y2 = to.y * pg.height;
            float w = 8.0f;
            pg.noFill();
            pg.stroke(0,0,0,20);
            drawArrowShape(pg, x1+3, y1+3, x2+3, y2+3, w, w-2);
            drawArrowShape(pg, x1+3, y1+3, x2+3, y2+3, w, w);
            drawArrowShape(pg, x1+3, y1+3, x2+3, y2+3, w, w+2);
            pg.stroke(255);
            pg.strokeWeight(w);
            drawArrowShape(pg, x1, y1, x2, y2, w, w);
            pg.strokeWeight(1.0f);  
        }

        private void drawArrowShape(PGraphics pg, float x1, float y1, float x2, float y2, float len, float w) {
            pg.strokeWeight(w);
            pg.line(x1, y1, x2, y2);
            pg.pushMatrix();
            pg.translate(x2, y2);
            float a = (float) Math.atan2(x1-x2, y2-y1);
            pg.rotate(a);
            pg.beginShape();
            pg.vertex(len * -1.5f, len * -2.5f);
            pg.vertex(0,0);
            pg.vertex(len * +1.5f, len * -2.5f);
            pg.endShape();
            pg.popMatrix();
        }

        public Location from, to;
        public float weight;
    }
    
}
