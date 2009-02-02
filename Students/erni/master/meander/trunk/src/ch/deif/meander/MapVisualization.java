package ch.deif.meander;

import processing.core.PApplet;

// TODO can we avoid subclassing Applet?
// TODO can we make visualizations composable?

@SuppressWarnings("serial")
public abstract class MapVisualization extends PApplet {
    
    protected final Parameters params;
    protected final Map map;

    public MapVisualization(Map map) {
        this.map = map;
        this.params = map.getParameters();
    }
    
    @Override
    public void setup() {
        frameRate(1);
    }
    
    @Override
    public abstract void draw();
    
}
