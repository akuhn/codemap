package org.codemap.communication.util;

import static org.codemap.util.CodemapIcons.MEEPLE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.codemap.Location;
import org.codemap.MapSelection;
import org.codemap.layers.SelectionOverlay;
import org.codemap.resources.MapValues;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;


public class CommunicationOverlay extends SelectionOverlay {
    
    private MapSelection selection;
    private Image image;
    private int diameter;
    private Rectangle bounds;

    public CommunicationOverlay(MapSelection communicationSelection) {
        selection = communicationSelection;
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return selection;
    }
    
    @Override
    public void paintBefore(MapValues map, GC gc) {
        image = descriptor(MEEPLE).createImage();
        bounds = image.getBounds();        
        diameter = (int) Math.sqrt(bounds.width * bounds.width + bounds.height * bounds.height);
        
        Device d = gc.getDevice();
        gc.setBackground(d.getSystemColor(SWT.COLOR_WHITE));
        gc.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
        gc.setLineWidth(1);
        gc.setAntialias(SWT.ON);        
    }
    
    @Override
    public void paintAfter(MapValues map, GC gc) {
        image.dispose();
        // gc maybe ...
        image = null;
        bounds = null;
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {       
        // draw semi-transparent background
        gc.setAlpha(196);
        gc.fillOval(each.px - diameter/2, each.py - diameter/2, diameter, diameter);
        gc.setAlpha(255);
        //draw image
        gc.drawImage(image, each.px - bounds.width/2, each.py - bounds.height/2);        
    }
}
