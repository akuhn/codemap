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
    
    public MapBuilder location(double x, double y, double h, String name) {
        locations.add(new Location(x, y, h, name));
        return this;
    }
    
    public Map build() {
        return new Map(params, locations);
    }
    
}
