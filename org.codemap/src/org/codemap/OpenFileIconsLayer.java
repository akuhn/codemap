package org.codemap;

import org.codemap.util.Icons;
import org.codemap.util.Resources;
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
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SelectionOverlay;

public class OpenFileIconsLayer extends SelectionOverlay {

    private ImageDescriptor imageDesc;
    private ILabelProvider provider;
    
    public OpenFileIconsLayer() {
        this.imageDesc = Icons.getImageDescriptor(Icons.FILE);
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
        Rectangle b = image.getBounds();
        int diameter = (int) Math.sqrt(b.width * b.width + b.height * b.height);
        gc.setAlpha(196);
        gc.fillOval(each.px - diameter/2, each.py - diameter/2, diameter, diameter);
        gc.setAlpha(255);
        if (image == null) {
            // needed to be able to destroy the image
            image = imageDesc.createImage();
            gc.drawImage(image, each.px - b.width/2, each.py - b.height/2);
            image.dispose();
        } else {
            gc.drawImage(image, each.px - b.width/2, each.py - b.height/2);            
        }
    }

}
