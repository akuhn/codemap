package org.codemap.util;

import static org.codemap.util.CodemapIcons.FILE;

import org.codemap.layers.SelectionOverlay;
import org.codemap.resources.MapValues;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;

public class OpenFileIconsLayer extends SelectionOverlay {

    private ImageDescriptor imageDesc;
    private ILabelProvider provider;
    
    public OpenFileIconsLayer() {
        this.imageDesc = CodemapIcons.descriptor(FILE);
        // TODO: dispose the LabelProvider when no longer needed
        this.provider = new WorkbenchLabelProvider();
    }
    
    @Override
    public MapSelection getSelection(MapValues map) {
        return map.openFilesSelection;
    }
    
    @Override
    public void paintBefore(MapValues map, GC gc) {
        Device d = gc.getDevice();
        gc.setBackground(d.getSystemColor(SWT.COLOR_WHITE));
        gc.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
        gc.setLineWidth(1);
        gc.setAntialias(SWT.ON);
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        IResource resource = Resources.asResource(each.getDocument());
        if (resource == null) return;
        Image image = provider.getImage(resource);
        if (image == null) {
            // fallback to default image, destroy the image after use
            image = imageDesc.createImage();
            drawImage(gc, image, each);
            image.dispose();
        } else {
            drawImage(gc, image, each);            
        }
    }

    private void drawImage(GC gc, Image image, Location each) {
        Rectangle b = image.getBounds();        
        int diameter = (int) Math.sqrt(b.width * b.width + b.height * b.height);
        // draw semi-transparent background
        gc.setAlpha(196);
        gc.fillOval(each.px - diameter/2, each.py - diameter/2, diameter, diameter);
        gc.setAlpha(255);
        //draw image
        gc.drawImage(image, each.px - b.width/2, each.py - b.height/2);
    }

}
