package ch.deif.meander.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import ch.deif.meander.AbstractMapSelection;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;

public abstract class SelectionOverlay extends SWTLayer {

	MapSelection selection;
	private Image image;
	
	protected void setImag(Image img) {
		image = img;
	}
	
	public Image getImage() {
		return image;
	}
	
	public AbstractMapSelection getSelection() {
		return selection;
	}
	
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
		for (Location each: getSelection().locationsOn(map)) {
			paintChild(map, gc, each);
		}
	}	
	
	public abstract void paintChild(MapInstance map, GC gc, Location each);


}
