package org.codemap.layers;

import org.codemap.Location;
import org.codemap.MapInstance;
import org.codemap.MapSelection;
import org.codemap.resources.MapValues;
import org.eclipse.swt.graphics.GC;


public abstract class SelectionOverlay extends Layer {

    public abstract MapSelection getSelection(MapValues map);

    @Override
    public final void paintMap(MapValues map, GC gc) {
        if (map.mapInstance.getValue() == null) return;
        paintBefore(map, gc);
        paintChildren(map, gc);
        paintAfter(map, gc);
    }

    /**
     * Hook for stuff to be done before we start painting.
     */
    public void paintBefore(MapValues map, GC gc) {
        // does nothing
    }
    
    /**
     * Hook for stuff to be done after painting.
     */    
    public void paintAfter(MapValues map, GC gc) {
        // does nothing        
    }    

    private final void paintChildren(MapValues map, GC gc) {
        MapSelection selection = this.getSelection(map);
        MapInstance mapInstance = map.mapInstance.getValue();
        if (selection == null || mapInstance == null) return;
        for (Location each: selection.locationsOn(map)) {
            paintChild(map, gc, each);
        }
    }	

    public abstract void paintChild(MapValues map, GC gc, Location each);


}
