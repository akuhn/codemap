package org.codemap.layers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codemap.resources.MapValues;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;


public class CompositeLayer extends Layer implements Iterable<Layer> {

    private List<Layer> children = new ArrayList<Layer>();

    @Override
    public void paintMap(MapValues map, GC gc) {
        for (Layer each: children ) each.paintMap(map, gc);
    }

    @Override
    public void setRoot(CodemapVisualization root) {
        super.setRoot(root);
        for (Layer each: children) {
            each.setRoot(root);
        }
    }

    public CompositeLayer add(Layer layer) {
        children.add(layer);
        layer.setRoot(getRoot());
        return this;
    }

    public CompositeLayer remove(Layer layer) {
        children.remove(layer);
        layer.setRoot(null);
        return this;
    }
    
    public boolean contains(Layer layer) {
        return children.contains(layer);
    }

    @Override
    public void dragDetected(DragDetectEvent e) {
        for (Layer each: children ) each.dragDetected(e);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        for (Layer each: children ) each.mouseDoubleClick(e);
    }

    @Override
    public void mouseDown(MouseEvent e) {
        for (Layer each: children ) each.mouseDown(e);
    }

    @Override
    public void mouseEnter(MouseEvent e) {
        for (Layer each: children ) each.mouseEnter(e);
    }

    @Override
    public void mouseExit(MouseEvent e) {
        for (Layer each: children ) each.mouseExit(e);
    }

    @Override
    public void mouseHover(MouseEvent e) {
        for (Layer each: children ) each.mouseHover(e);
    }

    @Override
    public void mouseMove(MouseEvent e) {
        for (Layer each: children ) each.mouseMove(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        for (Layer each: children ) each.mouseUp(e);
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        for (Layer each: children ) each.mouseScrolled(e);
    }

    @Override
    public Iterator<Layer> iterator() {
        return children.iterator();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        for (Layer each: children ) each.keyReleased(e);              
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        for (Layer each: children ) each.keyPressed(e);
    }  

}
