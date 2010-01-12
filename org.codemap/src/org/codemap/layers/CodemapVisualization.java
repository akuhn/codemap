package org.codemap.layers;

import static org.codemap.CodemapCore.colorScheme;

import org.codemap.CodemapCore;
import org.codemap.Location;
import org.codemap.MapInstance;
import org.codemap.mapview.NullTextUpdater;
import org.codemap.mapview.ITextUpdater;
import org.codemap.resources.MapValues;
import org.codemap.util.MColor;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;



public final class CodemapVisualization extends CompositeLayer implements PaintListener {
    

    private int offsetX;
    private int offsetY;
    private MapValues mapValues;
    private ITextUpdater textUpdater;
    
    private CodemapVisualization viz;
    private CompositeLayer shared;
    private YouAreHereOverlay youAreHere;
    private OpenFileIconsLayer openFiles;
    private CurrentSelectionOverlay selection;

    public CodemapVisualization(MapValues values) {
        this.mapValues = values;
        this.initializeVisualization();
    }
    
    private void initializeVisualization() {

        CompositeLayer foreground = new CompositeLayer();
        foreground.add(new LabelOverlay());
        foreground.add(selection = new CurrentSelectionOverlay());
        // TODO add methods to CompositeLayer to replace layers by eg an ID
        foreground.add(shared = new CompositeLayer());
        foreground.add(youAreHere = new YouAreHereOverlay());
        foreground.add(openFiles = new OpenFileIconsLayer());

        add(new Background());
        add(foreground);
    }
        
    public CompositeLayer getSharedLayer() {
        return shared;
    }

    public YouAreHereOverlay getYouAreHere() {
        return youAreHere;
    }

    public OpenFileIconsLayer getOpenFiles() {
        return openFiles;
    }

    public CurrentSelectionOverlay getSelection() {
        return selection;
    }    

    @Override
    public void paintControl(PaintEvent e) {
        if (!(e.widget instanceof Canvas)) throw new Error();
        try {
            GC gc = e.gc;
            Device device = gc.getDevice();
            Color waterColor = colorScheme().getWaterColor().asSWTColor(device);
            gc.setBackground(waterColor);
            gc.fillRectangle(gc.getClipping());
            
            Point bounds = ((Canvas) e.widget).getSize();
            offsetX = (bounds.x - mapValues.getSize()) / 2;
            offsetY = (bounds.y - mapValues.getSize()) / 2;
            Transform t = new Transform(device);
            t.translate(offsetX, offsetY);
            gc.setTransform(t);
            
            this.paintMap(mapValues, gc);
            
            t.dispose();
            waterColor.dispose();
        }
        catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CodemapVisualization add(Layer layer) {
        return (CodemapVisualization) super.add(layer);
    }

    @Override
    public CodemapVisualization remove(Layer layer) {
        return (CodemapVisualization) super.remove(layer);
    }

    private void addOffsetAndThisToEvent(MouseEvent e) {
        e.x -= offsetX;
        e.y -= offsetY;
        e.data = this;
    }
    
    @Override
    public void mouseMove(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        this.updateTooltip(e);
        super.mouseMove(e);
    }

    private void updateTooltip(MouseEvent e) {
        MapInstance map = mapValues.mapInstance.getValue();
        if (map == null) return;
        Location nearestNeighbor = map.nearestNeighbor(e.x, e.y);
        
        boolean noName = map.isEmpty() || !map.containsPoint(e.x, e.y) || nearestNeighbor == null;
        String name = noName ? null : nearestNeighbor.getName();
        textUpdater.updateNearestNeighbor(name);
    }

    @Override
    public void dragDetected(DragDetectEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.dragDetected(e);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseDoubleClick(e);
    }

    @Override
    public void mouseDown(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseDown(e);
    }

    @Override
    public void mouseEnter(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseEnter(e);
    }

    @Override
    public void mouseExit(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseExit(e);
    }

    @Override
    public void mouseHover(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseHover(e);
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseScrolled(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseUp(e);
    }

    protected static MapValues mapValues(MouseEvent e) {
        return ((CodemapVisualization) e.data).mapValues;
    }

    public void removeUpdater() {
        textUpdater = new NullTextUpdater();
    }

    public void setUpdater(ITextUpdater updater) {
        textUpdater = updater;
    }
}
