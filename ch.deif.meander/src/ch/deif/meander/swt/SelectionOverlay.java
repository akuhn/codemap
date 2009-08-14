package ch.deif.meander.swt;

import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;

public abstract class SelectionOverlay extends SWTLayer {

    public abstract MapSelection getSelection(MapValues map);

    @Override
    public final void paintMap(MapValues map, GC gc) {
        if (map.mapInstance.getValue() == null) return;
        paintBefore(map,gc);
        paintChildren(map, gc);
    }

    public void paintBefore(MapValues map, GC gc) {
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
