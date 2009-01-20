package ch.deif.meander;

import java.util.Arrays;
import java.util.Collection;

public class MapDescription {

    public Collection<Location> locations;
    private Parameters parameters;
    
    public MapDescription(Parameters parameters, Location... locations) {
        this.parameters = parameters;
        this.locations = Arrays.asList(locations);
    }

    public Map generateMap() {
        return new Map(this);
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Iterable<Location> locations() {
        return locations;
    }
    
}
