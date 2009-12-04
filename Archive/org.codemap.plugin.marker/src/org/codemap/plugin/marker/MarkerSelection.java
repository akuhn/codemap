package org.codemap.plugin.marker;

import java.util.HashMap;
import java.util.Map;

import ch.deif.meander.MapSelection;

public class MarkerSelection {
    
    private MapSelection selection;
    private HashMap<String, Integer> severityMap;

    public MarkerSelection() {
        selection = new MapSelection();
        severityMap = new HashMap<String, Integer>();
    }

    public int getSeverity(String document) {
        return severityMap.get(document);
    }

    public void addAll(Map<String, Integer> map) {
        severityMap.putAll(map);
        selection.addAll(map.keySet());
    }

    public void remove(String identifier) {
        severityMap.remove(identifier);
        selection.remove(identifier);
    }

    public void add(String identifier, int severity) {
        severityMap.put(identifier, severity);
        selection.add(identifier);
    }

    public void clear() {
        severityMap.clear();
        selection.clear();
    }

    public MapSelection getSelection() {
        return selection;
    }

}
