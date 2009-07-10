package ch.deif.meander.swt;

import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;

public abstract class SelectionOverlay extends SWTLayer {

	protected MapSelection selection;

	public SelectionOverlay setSelection(MapSelection selection) {
		this.selection = selection;
		return this;
	}

	@Override
	public final void paintMap(MapInstance map, GC gc) {
		paintBefore(map,gc);
		paintChildren(map, gc);
	}

	public void paintBefore(MapInstance map, GC gc) {
		// does nothing
	}

	private final void paintChildren(MapInstance map, GC gc) {
		for (Location each: selection.locationsOn(map)) {
			paintChild(gc, each);
		}
	}	
	
	public abstract void paintChild(GC gc, Location each);

	public MapSelection getSelection() {
		return selection;
	}

}
