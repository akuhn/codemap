package ch.deif.meander;

import processing.core.PApplet;

@SuppressWarnings("serial")
public abstract class MapVisualization extends PApplet {

    protected final Parameters params;
    protected final Map map;

    public MapVisualization(Map map) {
        this.map = map;
        this.params = map.getParameters();
    }
    
    @Override
    public abstract void draw();
    
}
