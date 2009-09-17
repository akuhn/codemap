package edu.berkeley.guir.prefusex.controls;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.event.ControlAdapter;

/**
 * Rotates the display.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class RotationControl extends ControlAdapter {

    private int xLast, yLast;
    private boolean repaint = true;
    
    /**
     * Creates a new rotation control that issues repaint requests as an item
     * is dragged.
     */
    public RotationControl() {
        this(true);
    } //
    
    /**
     * Creates a new rotation control that optionally issues repaint requests
     * as an item is dragged.
     * @param repaint indicates whether or not repaint requests are issued
     *  as rotation events occur. This can be set to false if other activities
     *  (for example, a continuously running force simulation) are already
     *  issuing repaint events.
     */
    public RotationControl(boolean repaint) {
        this.repaint = repaint;
    } //
    
    public void mousePressed(MouseEvent e) {
        if ( SwingUtilities.isLeftMouseButton(e) ) {
            Display display = (Display)e.getComponent();
            display.setCursor(
                Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            xLast = e.getX(); yLast = e.getY();
        }
    } //
    
    public void mouseDragged(MouseEvent e) {
        if ( SwingUtilities.isLeftMouseButton(e) ) {
            Display display = (Display)e.getComponent();
            double scale = display.getScale();
            
            int x = e.getX(), y = e.getY();
            int dx = x-xLast;
            int dy = y-yLast;

            //double angle = Math.atan2(dy,dx);
            double angle = ((double)dx)/40;
            
            AffineTransform at = display.getTransform();
            at.rotate(angle);
            try {
                display.setTransform(at);
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
            
            yLast = y;
            xLast = x;
            if ( repaint )
                display.repaint();
        }
    } //
    
    public void mouseReleased(MouseEvent e) {
        if ( SwingUtilities.isLeftMouseButton(e) ) {
            e.getComponent().setCursor(Cursor.getDefaultCursor());
        }
    } //
    
} // end of class ZoomControl
