package org.codemap.layers;

import org.codemap.resources.MapValues;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;


public class Background extends Layer {

    @Override
    public void paintMap(MapValues map, GC gc) {
        Image buffer = map.background.getValue();
        if (buffer == null) return;
        gc.drawImage(buffer, 0, 0);
    }

}
