package ch.deif.meander.swt;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.DEMAlgorithm;

public class RawElevationBackground {

    private Image image;

    public void paintMap(MapInstance map, PaintEvent e) {
        assert e.x == e.gc.getClipping().x;
        if (image != null) e.gc.drawImage(image, 0, 0);
        float[][] DEM = map.get(DEMAlgorithm.class);
        Display display = Display.getCurrent();
        image = new Image(display, map.width, map.height);
        GC gc = new GC(image);
        double maxElevation = map.maxElevation();
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                int scale = (int) (DEM[x][y] / maxElevation * 255);
                if (scale > 255) scale = 255;
                scale = 255 - scale;
                Color color = new Color(display, scale, scale, scale);
                gc.setForeground(color);
                color.dispose();
                gc.drawPoint(x, y);
            }
        }
        e.gc.drawImage(image, 0, 0);
    }

}
