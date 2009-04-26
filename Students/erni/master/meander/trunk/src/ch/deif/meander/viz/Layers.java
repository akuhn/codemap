package ch.deif.meander.viz;

import java.util.LinkedList;

import processing.core.PGraphics;
import ch.deif.meander.Map;


public class Layers extends MapVisualization<Drawable> {

    private MapVisualization<?> background;
    private LinkedList<Drawable> layers;
    
    public Layers(Map map) {
        super(map);
        background = new SketchVisualization(map);
        layers = new LinkedList<Drawable>();
    }
    
    public void add(Drawable overlay) {
        layers.add(overlay);
    }

    @Override
    public void drawThis(PGraphics pg) {
        background.draw(pg);
        for (Drawable overlay: layers) overlay.draw(pg);
    }

    public void useHillshading() {
        background = new HillshadeVisualization(map);
    }


}
