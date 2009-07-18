package ch.deif.meander.swt;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.MapInstance;

public class Background extends SWTLayer {

	public List<SWTLayer> children = new LinkedList<SWTLayer>();
	private Image buffer;
	
	@Override
	public void paintMap(MapInstance map, GC gc) {
		if (needsRepaint(map)) buffer = makeBuffer(map);
		gc.drawImage(buffer, 0, 0);
	}

	private boolean needsRepaint(MapInstance map) {
		return buffer == null || buffer.getBounds().width != map.getWidth();
	}

	private Image makeBuffer(MapInstance map) {
		Display display = Display.getCurrent();
		Image image = new Image(display, map.width, map.height);
		GC gc = new GC(image);
		for (SWTLayer each: children) each.paintMap(map, gc);
		gc.dispose();
		
		//ImageLoader imageLoader = new ImageLoader();
		//imageLoader.data = new ImageData[] { image.getImageData() };
		//imageLoader.save("background.jpg",SWT.IMAGE_JPEG);
		
		return image;
	}

}
