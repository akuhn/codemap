package org.codemap;

import org.codemap.util.Icons;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
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

    private Image image;
    private ILabelProvider provider;
    
    public OpenFileIconsLayer() {
        this.image = Icons.getImage(Icons.FILE);
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
        if (image == null) image = this.image;
        Rectangle b = image.getBounds();
        int d = (int) Math.sqrt(b.width * b.width + b.height * b.height);
        gc.setAlpha(196);
        gc.fillOval(each.px - d/2, each.py - d/2, d, d);
        gc.setAlpha(255);
        gc.drawImage(image, each.px - b.width/2, each.py - b.height/2);
    }

}
