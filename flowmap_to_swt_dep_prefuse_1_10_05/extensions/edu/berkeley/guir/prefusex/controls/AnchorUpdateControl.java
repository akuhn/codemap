package edu.berkeley.guir.prefusex.controls;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefuse.event.ControlAdapter;

/**
 * Follows the mouse cursor, updating the anchor parameter for any number
 * of layout instances to match the current cursor position. Will also
 * run a given activity in response to cursor updates.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class AnchorUpdateControl extends ControlAdapter {
    
    private Layout[] m_layouts;
    private Activity m_activity;
    private Point2D  m_tmp = new Point2D.Float();
    
    public AnchorUpdateControl(Layout layout) {
        this(layout,null);
    } //
    
    public AnchorUpdateControl(Layout layout, Activity update) {
        this(new Layout[] {layout}, update);
    } //
    
    public AnchorUpdateControl(Layout[] layout, Activity update) {
        m_layouts = (Layout[])layout.clone();
        m_activity = update;
    } //
    
    public void mouseExited(MouseEvent e) {
        for ( int i=0; i<m_layouts.length; i++ ) 
            m_layouts[i].setLayoutAnchor(null);
        if ( m_activity != null )
            m_activity.runNow();
    } //
    
    public void mouseMoved(MouseEvent e) {
        moveEvent(e);
    } //
    
    public void mouseDragged(MouseEvent e) {
        moveEvent(e);
    } //
    
    public void moveEvent(MouseEvent e) {
        Display d = (Display)e.getSource();
        d.getAbsoluteCoordinate(e.getPoint(), m_tmp);
        for ( int i=0; i<m_layouts.length; i++ ) 
            m_layouts[i].setLayoutAnchor(m_tmp);
        if ( m_activity != null )
            m_activity.runNow();
    } //
        
} // end of class AnchorUpdateControl
