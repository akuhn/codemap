package org.codemap.layers;

import org.codemap.resources.MapValues;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;


public abstract class Layer implements 
        MouseListener, 
        MouseMoveListener, 
        MouseTrackListener, 
        MouseWheelListener,
        DragDetectListener {

    private CodemapVisualization root;

    CodemapVisualization getRoot() {
        return root;
    }

    public void setRoot(CodemapVisualization root) {
        this.root = root;
    }

    public abstract void paintMap(MapValues map, GC gc);

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseDown(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseUp(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseMove(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseEnter(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExit(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseHover(MouseEvent e) {
        // do nothing
    }

    @Override
    public void dragDetected(DragDetectEvent e) {
        // do nothing
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        // do nothing
    }

    protected void redraw(MouseEvent e) {
        if (e.widget instanceof Control) ((Control) e.widget).redraw();
    }

}
