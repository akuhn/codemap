package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;

public class OpenFilesOverlay extends SelectionOverlay {

    protected final int SELECTION_SIZE = 12;
    protected final int POINT_STROKE = 1;	

    @Override
    public void paintBefore(MapValues map, GC gc) {
        Device device = Display.getCurrent();
        gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
        gc.setLineWidth(POINT_STROKE);
        gc.setAlpha(255);
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        gc.fillOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
                SELECTION_SIZE, SELECTION_SIZE);
        gc.drawOval(each.px - SELECTION_SIZE/2, each.py - SELECTION_SIZE/2,
                SELECTION_SIZE, SELECTION_SIZE);
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return map.openFilesSelection;
    }

}
