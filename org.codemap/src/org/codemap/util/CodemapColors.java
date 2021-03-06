package org.codemap.util;

import static org.codemap.CodemapCore.colorScheme;

import java.util.HashMap;
import java.util.Map;

import org.codemap.Point;


public class CodemapColors extends MapScheme<MColor> {

    private Map<String, MColor> forLocation;

    public CodemapColors() {
        this(colorScheme().getHillColor());
    }

    public CodemapColors(MColor defaultColor) {
        super(defaultColor);
        forLocation = new HashMap<String, MColor>();
    }

    public void setColor(String identifier, MColor color) {
        forLocation.put(identifier, color);
    }

    public void clearColors() {
        forLocation.clear();
    }

    @Override
    public MColor forLocation(Point location) {
        String identifier = location.getDocument();
        if (forLocation.containsKey(identifier)) {
            return forLocation.get(identifier);
        }
        return super.forLocation(location);
    }



}
