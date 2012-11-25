package edu.berkeley.guir.prefusex.controls;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.event.ControlAdapter;

/**
 * Pans the display, changing the viewable region of the visualization.
 * Panning is accomplished by clicking on the background of a visualization
 * with the left mouse button and then dragging.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class PanControl extends ControlAdapter {

    private int xDown, yDown;
    private boolean repaint = true;
    
    /**
     * Creates a new panning control that issues repaint requests as an item
     * is dragged.
     */
    public PanControl() {
        this(true);
    } //
    
    /**
     * Creates a new panning control that optionally issues repaint requests
     * as an item is dragged.
     * @param repaint indicates whether or not repaint requests are issued
     *  as pan events occur. This can be set to false if other activities
     *  (for example, a continuously running force simulation) are already
     *  issuing repaint events.
     */
    public PanControl(boolean repaint) {
        this.repaint = repaint;
    } //    
    
    public void mousePressed(MouseEvent e) {
        if ( SwingUtilities.isLeftMouseButton(e) ) {
            e.getComponent().setCursor(
                Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            xDown = e.getX();
            yDown = e.getY();
        }
    } //
    
    public void mouseDragged(MouseEvent e) {
        if ( SwingUtilities.isLeftMouseButton(e) ) {
            Display display = (Display)e.getComponent();
            int x = e.getX(), y = e.getY();
            int dx = x-xDown, dy = y-yDown;
            display.pan(dx,dy);
            xDown = x;
            yDown = y;
            if ( repaint )
                display.repaint();
        }
    } //
    
    public void mouseReleased(MouseEvent e) {
        if ( SwingUtilities.isLeftMouseButton(e) ) {
            e.getComponent().setCursor(Cursor.getDefaultCursor());
            xDown = -1;
            yDown = -1;
        }
    } //
    
} // end of class PanControl
