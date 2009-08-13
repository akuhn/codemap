package ch.deif.meander.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import ch.deif.meander.map.MapValues;

public class Background extends SWTLayer {

    @Override
    public void paintMap(MapValues map, GC gc) {
        Image buffer = map.background.value();
        if (buffer == null) return;
        gc.drawImage(buffer, 0, 0);
    }

}
