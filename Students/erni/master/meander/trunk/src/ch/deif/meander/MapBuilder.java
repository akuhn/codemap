package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;

public class MapBuilder {

    private Parameters params;
    private Collection<Location> locations;
    
    public MapBuilder() {
        params = new Parameters();
        locations = new ArrayList<Location>();
    }
    
    public MapBuilder size(int width, int height) {
        params.width = width;
        params.height = height;
        return this;
    }
    
    public MapBuilder location(double xNormed, double yNormed, double h) {
        locations.add(new Location(xNormed, yNormed, h));
        return this;
    }
    
    public Map build() {
        return new Map(params, locations);
    }
    
}
