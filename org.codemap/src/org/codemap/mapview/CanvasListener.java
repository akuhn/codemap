package org.codemap.mapview;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import ch.deif.meander.swt.CodemapVisualization;

public class CanvasListener implements PaintListener, MouseListener,
        MouseMoveListener, MouseTrackListener, MouseWheelListener,
        DragDetectListener {

    private CodemapVisualization visualization;

    public CanvasListener(Canvas newCanvas) {
        newCanvas.addPaintListener(this);
        newCanvas.addMouseListener(this);
        newCanvas.addMouseMoveListener(this);
        newCanvas.addMouseTrackListener(this);
        newCanvas.addMouseWheelListener(this);
        newCanvas.addDragDetectListener(this);
    }

    public void setVisualization(CodemapVisualization visualization) {
        this.visualization = visualization;
    }

    public CodemapVisualization getVisualization() {
        return visualization;
    }

    @Override
    public void paintControl(PaintEvent e) {
        if (visualization != null)
            visualization.paintControl(e);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        if (visualization != null)
            visualization.mouseDoubleClick(e);
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (visualization != null)
            visualization.mouseDown(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (visualization != null)
            visualization.mouseUp(e);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (visualization != null)
            visualization.mouseMove(e);
    }

    @Override
    public void mouseEnter(MouseEvent e) {
        if (visualization != null)
            visualization.mouseEnter(e);
    }

    @Override
    public void mouseExit(MouseEvent e) {
        if (visualization != null)
            visualization.mouseExit(e);
    }

    @Override
    public void mouseHover(MouseEvent e) {
        if (visualization != null)
            visualization.mouseHover(e);
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        if (visualization != null)
            visualization.mouseScrolled(e);
    }

    @Override
    public void dragDetected(DragDetectEvent e) {
        if (visualization != null)
            visualization.dragDetected(e);
    }
}