package org.codemap.plugin.marker;

import static org.codemap.util.Icons.ERROR;
import static org.codemap.util.Icons.INFO;
import static org.codemap.util.Icons.WARNING;

import org.codemap.util.Icons;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SelectionOverlay;

public class MarkersOverlay extends SelectionOverlay {

    private MarkerSelection markerSelection;

    @Override
    public void paintBefore(MapValues map, GC gc) {
        Device device = gc.getDevice();
        Color orange = new Color(device, 255, 200, 0);
        gc.setBackground(orange);
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        int severity = markerSelection.getSeverity(each.getDocument());
        Image image = getImageDescriptor(severity).createImage();
        Rectangle offset = image.getBounds();		
        int offset_x = offset.width/2;
        int offset_y = offset.height/2;		
        gc.drawImage(image, each.px - offset_x, each.py - offset_y);
        image.dispose();
    }

    private ImageDescriptor getImageDescriptor(int severity) {
        switch (severity) {
        case IMarker.SEVERITY_ERROR:
            return Icons.getImageDescriptor(ERROR);
        case IMarker.SEVERITY_WARNING:
            return Icons.getImageDescriptor(WARNING);
        default:
            return Icons.getImageDescriptor(INFO);
        }
    }

    public void setMarkerSelection(MarkerSelection selection) {
        markerSelection = selection;
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return markerSelection.getSelection();
    }

}
