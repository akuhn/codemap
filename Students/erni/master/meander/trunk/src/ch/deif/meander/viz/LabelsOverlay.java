package ch.deif.meander.viz;

import ch.deif.meander.Location;
import ch.deif.meander.Map;
import processing.core.PFont;
import processing.core.PGraphics;

public class LabelsOverlay extends MapVisualization {

    private PFont PFONT = new PFont(PFont.findFont("Times New Roman"), true, PFont.DEFAULT_CHARSET);

    public LabelsOverlay(Map map) {
        super(map);
        for (Location each: map.locations()) {
            Label l = new Label(each.getName());
            addChild(l);
            l.x = each.px();
            l.y = each.py();
            l.size = (float) each.elevation();
        }
    }
    
 
    @Override
    public void draw(PGraphics pg) {
        pg.textFont(PFONT);
        pg.fill(255,0,0);
        pg.textSize(20);

    }

}
