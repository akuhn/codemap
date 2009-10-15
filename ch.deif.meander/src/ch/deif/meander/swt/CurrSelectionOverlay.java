package ch.deif.meander.swt;

import static ch.deif.meander.swt.CodemapVisualization.fireEvent;
import static ch.deif.meander.swt.CodemapVisualization.mapValues;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import ch.akuhn.util.Assert;
import ch.akuhn.util.List;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.MapSelection;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.map.MapValues;


public class CurrSelectionOverlay extends SelectionOverlay {

    protected final int POINT_STROKE = 2;

    public static final String EVT_DOUBLE_CLICKED = "doubleClicked";
    public static final String EVT_SELECTION_CHANGED = "selectionChanged";

    private boolean isDragging = false;
    private Point dragStart;
    private Point dragStop;

    @Override
    public void dragDetected(DragDetectEvent e) {
        isDragging = true;
        dragStop = dragStart = new Point(e.x, e.y);	
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        MapInstance map = CodemapVisualization.mapValues(e).mapInstance.getValue();
        if (map == null) return;
        Location neighbor = map.nearestNeighbor(e.x, e.y);
        fireEvent(e, EVT_DOUBLE_CLICKED, neighbor);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (!isDragging) return;
        dragStop = new Point(e.x, e.y);	
        this.redraw(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (isDragging) updateSelection(mapValues(e));
        else if (e.count == 1) handleSingleClick(e);
        isDragging = false;
        this.redraw(e);
        fireEvent(e, EVT_SELECTION_CHANGED, getSelection(mapValues(e)));
    }

    private void handleSingleClick(MouseEvent e) {
        MapInstance mapInstance = mapValues(e).mapInstance.getValue();
        if (mapInstance == null) return;
        
        String neighborIdentifier = mapInstance.nearestNeighbor(e.x, e.y).getDocument();
        Assert.notNull(neighborIdentifier);
        if (isShiftDown(e)) {
            getSelection(mapValues(e)).add(neighborIdentifier);
        }
        else {
            getSelection(mapValues(e)).replaceAll(List.of(neighborIdentifier));            
        }
    }

    private boolean isShiftDown(MouseEvent e) {
        return (e.stateMask & SWT.SHIFT) != 0;
    }

    @Override
    public void paintBefore(MapValues map, GC gc) {
        gc.setLineWidth(POINT_STROKE);
        if (isDragging) {
            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_LIST_SELECTION));
            
            updateSelection(map);
            int deltaX = dragStop.x - dragStart.x;
            int deltaY = dragStop.y - dragStart.y;
            gc.drawRectangle(dragStart.x, dragStart.y, deltaX, deltaY);
        }
    }

    @Override
    public void paintChild(MapValues map, GC gc, Location each) {
        Device device = gc.getDevice();
        int mapSize = map.mapSize.getValue();
        int magic_r = (int) (each.getElevation() * 2 * mapSize / DEMAlgorithm.MAGIC_VALUE);
        
        gc.setAlpha(128);        
        gc.setBackground(device.getSystemColor(SWT.COLOR_LIST_SELECTION));
        gc.fillOval(each.px - magic_r, each.py - magic_r, magic_r * 2, magic_r * 2);
        
        gc.setAlpha(255);        
        gc.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
        
        gc.drawOval(each.px-magic_r, each.py-magic_r, magic_r*2, magic_r*2);
    }
    
    private void updateSelection(MapValues map) {
        MapInstance mapInstance = map.mapInstance.getValue();
        if (mapInstance == null) return;
        int minX = Math.min(dragStart.x, dragStop.x);
        int minY = Math.min(dragStart.y, dragStop.y);
        int maxX = Math.max(dragStart.x, dragStop.x);
        int maxY = Math.max(dragStart.y, dragStop.y);		
        Collection<String> ids = new ArrayList<String>();
        for (Location each : mapInstance.locations()) {
            if (each.px < maxX && each.px > minX && each.py < maxY && each.py > minY) {			
                ids.add(each.getDocument());
            }
        }
        getSelection(map).replaceAll(ids);
    }

    @Override
    public MapSelection getSelection(MapValues map) {
        return map.currentSelection;
    }

}
