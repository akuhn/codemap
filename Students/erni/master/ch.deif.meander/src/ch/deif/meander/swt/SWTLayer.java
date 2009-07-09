package ch.deif.meander.swt;

import org.eclipse.swt.graphics.GC;

import ch.deif.meander.MapInstance;

public abstract class SWTLayer {

	public abstract void paintMap(MapInstance map, GC gc);
	
}
