package org.codemap.plugin.communication.util;

import static org.codemap.plugin.communication.util.ECFPluginIcons.MEEPLE;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SelectionOverlay;

public class CommunicationOvleray extends SelectionOverlay {
    
    private MapSelection selection;
    private ImageDescriptor imageDescriptor;
    private Image image;
    private int diameter;
    private Rectangle bounds;

    public CommunicationOvleray(MapSelection communicationSelection) {
        selection = communicationSelection;
        imageDescriptor = new ECFPluginIcons().descriptor(MEEPLE);
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return selection;
    }
    
    @Override
    public void paintBefore(MapValues map, GC gc) {
        image = imageDescriptor.createImage();
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
