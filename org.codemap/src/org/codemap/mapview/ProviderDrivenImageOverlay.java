package org.codemap.mapview;

import org.codemap.util.Resources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.swt.ImageOverlay;

public class ProviderDrivenImageOverlay extends ImageOverlay {

	private ILabelProvider provider;

	public ProviderDrivenImageOverlay(Image def, ILabelProvider labelProvider) {
		super(def);
		provider = labelProvider;
	}

	@Override
	public void paintChild(MapInstance map, GC gc, Location each) {
		Image image = provider.getImage(Resources.asJavaElement(each.getDocument()));
		if (image == null) {
			super.paintChild(map, gc, each);
			return;
		}
		Rectangle offset = image.getBounds();		
		int offset_x = offset.width/2;
		int offset_y = offset.height/2;
		gc.drawImage(image, each.px - offset_x, each.py - offset_y);		
	}

}
