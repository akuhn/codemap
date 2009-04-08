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
            float w = 40.0f;
            pg.strokeWeight(w);
            pg.line(x1, y1, x2, y2);
            pg.pushMatrix();
            pg.translate(x2, y2);
            float a = (float) Math.atan2(x1-x2, y2-y1);
            pg.rotate(a);
            pg.beginShape();
            pg.vertex(w * -1.5f, w * -2f);
            pg.vertex(0,0);
            pg.vertex(w * +1.5f, w * -2f);
            pg.endShape();
            pg.popMatrix();
            pg.strokeWeight(1.0f);  
            pg.fill(255);
            pg.rect(x1-2,y1-2,4,4);
            pg.rect(x1-100,y1-100,200,200);
        }

        public Location from, to;
        public float weight;
    }
    
}
