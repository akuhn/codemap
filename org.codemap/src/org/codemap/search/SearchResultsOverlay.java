package org.codemap.search;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SelectionOverlay;

public class SearchResultsOverlay extends SelectionOverlay {
    
    protected static final int NEEDLEWIDTH = 3;
    protected static final int NEEDLEHEIGHT = 10;
    protected static final int HEAD_Y_OFFSET = 20;
    protected static final int CP_X_OFFSET = 10;
    protected static final int CP_Y_OFFSET = 17;
    protected static final int GAP_Y = 4;
    protected static final int SELECTION_STROKE = 1;
    
    private SearchResultController searchResultController;
    private Path path;

    public SearchResultsOverlay(SearchResultController searchResultController) {
        super();
        this.searchResultController = searchResultController;
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        // draw background
        Device device = gc.getDevice();
        
        // get transform
        Transform save = new Transform(device);
        gc.getTransform(save);
        Transform t = new Transform(device);
        gc.getTransform(t);
        
        // push stack, first translate is applied last
        t.translate(each.px, each.py);
        t.rotate(27);
        t.translate(0, GAP_Y);
        gc.setTransform(t);
        
        gc.fillPath(path);
        gc.drawPath(path);
        
        // restore old transform
        gc.setTransform(save);
        t.dispose();
        save.dispose();
    }

    @Override
    public void paintBefore(MapValues map, GC gc) {
        Device device = gc.getDevice();
        
        path = new Path(device);
        path.moveTo(0, 0);
        path.lineTo(-NEEDLEWIDTH, NEEDLEHEIGHT);
        path.quadTo(-CP_X_OFFSET, +CP_Y_OFFSET, 0, HEAD_Y_OFFSET);
        path.quadTo(CP_X_OFFSET, CP_Y_OFFSET, NEEDLEWIDTH, NEEDLEHEIGHT);
        path.lineTo(0, 0);        
        path.close();
        
        gc.setAlpha(255);
        Color red = new Color(device, 252, 99, 58);
        gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(red);
        red.dispose();
        gc.setLineWidth(SELECTION_STROKE);
    }
    
    @Override
    public void paintAfter(MapValues map, GC gc) {
        super.paintAfter(map, gc);
        if (path == null) return;
        path.dispose();
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return searchResultController.getSearchSelection();
    }


}
