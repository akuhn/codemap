package edu.berkeley.guir.prefusex.controls;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.event.ControlAdapter;

/**
 * Changes the location of a whole subtree when dragged on screen. This is
 * similar to the {@link DragControl DragControl} class, except that it
 * moves the entire visible subtree rooted at an item, rather than just the
 * item itself.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class SubtreeDragControl extends ControlAdapter {

    private VisualItem activeItem;
    private Point2D down = new Point2D.Double();
    private Point2D tmp = new Point2D.Double();
    private boolean wasFixed;
    private boolean repaint = true;
    
    /**
     * Creates a new subtree drag control that issues repaint requests as an
     * item is dragged.
     */
    public SubtreeDragControl() {
    } //
    
    /**
     * Creates a new subtree drag control that optionally issues repaint 
     * requests as an item is dragged.
     * @param repaint indicates whether or not repaint requests are issued
     *  as drag events occur. This can be set to false if other activities
     *  (for example, a continuously running force simulation) are already
     *  issuing repaint events.
     */
    public SubtreeDragControl(boolean repaint) {
        this.repaint = repaint;
    } //
    
    public void itemEntered(VisualItem item, MouseEvent e) {
        if ( !(item instanceof NodeItem) ) return;
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    } //
    
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( !(item instanceof NodeItem) ) return;
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
    } //
    
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( !(item instanceof NodeItem) ) return;
        Display d = (Display)e.getComponent();
        down = d.getAbsoluteCoordinate(e.getPoint(), down);
        activeItem = item;
        wasFixed = item.isFixed();
        item.setFixed(true);
    } //
    
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( !(item instanceof NodeItem) ) return;
        activeItem = null;
        item.setFixed(wasFixed);
    } //
    
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( !(item instanceof NodeItem) ) return;
        Display d = (Display)e.getComponent();
        tmp = d.getAbsoluteCoordinate(e.getPoint(), tmp);
        double dx = tmp.getX()-down.getX();
        double dy = tmp.getY()-down.getY();
        updateLocations((NodeItem)item, dx, dy);
        down.setLocation(tmp);
        if ( repaint )
            item.getItemRegistry().repaint();
    } //
    
    private void updateLocations(NodeItem n, double dx, double dy) {
        Point2D p = n.getLocation();
        n.updateLocation(p.getX()+dx,p.getY()+dy);
        n.setLocation(p.getX()+dx,p.getY()+dy);
        for ( int i=0; i<n.getChildCount(); i++ )
            updateLocations((NodeItem)n.getChild(i),dx,dy);
    } //
    
} // end of class SubtreeDragControl
