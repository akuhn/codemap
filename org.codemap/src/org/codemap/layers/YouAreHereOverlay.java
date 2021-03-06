package org.codemap.layers;

import org.codemap.Location;
import org.codemap.MapSelection;
import org.codemap.resources.MapValues;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;


public class YouAreHereOverlay extends SelectionOverlay {

    private static final int GAP = 4;
    private static final int ARROW_WIDTH = 8;
    private static final int ARROW_HEIGHT = 10;
    private static final int PADDING_X = 6;
    private static final int PADDING_Y = 1;

    @Override
    public void paintBefore(MapValues map, GC gc) {
        Device device = gc.getDevice();
        gc.setLineWidth(1);
        gc.setAlpha(255);
        gc.setForeground(device.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        gc.setBackground(device.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location location) {
        Device device = gc.getDevice();
        gc.setFont(device.getSystemFont());
        String name = location.getName();
        Point e = gc.stringExtent(name);
        e.x += PADDING_X * 2;
        e.y += PADDING_Y * 2;
        int[] polygon = new int[] { 
                0, 0,
                e.x, 0,
                e.x, e.y,
                (e.x + ARROW_WIDTH) / 2, e.y,
                e.x / 2, e.y + ARROW_HEIGHT,
                (e.x - ARROW_WIDTH) / 2, e.y,
                0, e.y };
        Transform save = new Transform(device);
        gc.getTransform(save);
        Transform t = new Transform(device);
        gc.getTransform(t);
        t.translate(location.px - e.x/2, location.py - e.y - ARROW_HEIGHT - GAP);
        gc.setTransform(t);
        gc.fillPolygon(polygon);
        gc.drawPolygon(polygon);
        gc.drawText(name, PADDING_X, PADDING_Y);
        gc.setTransform(save);
        t.dispose();
        save.dispose();

        // TODO learn from class RenameInformationPopup how to open a custom popup, dream on...

    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return map.youAreHereSelection;
    }
}