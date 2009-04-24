package ch.deif.meander.viz;

import java.util.LinkedList;

import ch.deif.meander.Map;

import processing.core.PGraphics;


public class Layers extends MapVisualization {

    private MapVisualization background;
    private LinkedList<MapVisualization> layers;
    
    public Layers(Map map) {
        super(map);
        background = new SketchVisualization(map);
        layers = new LinkedList<MapVisualization>();
    }
    
    public void add(MapVisualization overlay) {
        layers.add(overlay);
    }

    @Override
    public void draw(PGraphics pg) {
        background.draw(pg);
        for (MapVisualization overlay: layers) overlay.draw(pg);
    }

    public void useHillshading() {
        background = new HillshadeVisualization(map);
    }


}
