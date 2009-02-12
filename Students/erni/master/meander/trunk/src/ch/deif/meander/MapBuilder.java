package ch.deif.meander;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.hapax.corpus.Document;

public class MapBuilder {

    private Parameters params;
    private Collection<Location> locations;
    private String name;
    
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

    public MapBuilder location(double xNormed, double yNormed, double h, Document document) {
        Location loc = new Location(xNormed, yNormed, h);
        loc.document = document;
        locations.add(loc);
        return this;
    }
    
    public Map build() {
        Map map = new Map(params, locations);
        map.name = name;
        return map;
    }

    public MapBuilder name(String versionName) {
        name = versionName;
        return this;
    }
    
}
