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
            pg.line(x1, y1, x2, y2);
            pg.pushMatrix();
            pg.translate(x2, y2);
            float a = (float) Math.atan2(x1-x2, y2-y1);
            pg.rotate(a);
            pg.line(0, 0, -8, -12);
            pg.line(0, 0, +8, -12);
            pg.popMatrix();
        }

        public Location from, to;
        public float weight;
    }
    
}
