package edu.berkeley.guir.prefuse.action.assignment;

import java.awt.Insets;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;

/**
 * Abstract class providing convenience methods for graph layout algorithms.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class Layout extends AbstractAction {

    protected Rectangle2D m_bounds = null;
    protected Point2D     m_anchor = null;
    private   Insets      m_insets = new Insets(0,0,0,0);
    
    private   double[]    m_bpts   = new double[4];
    private   Rectangle2D m_tmp    = new Rectangle2D.Double();
    
    public Rectangle2D getLayoutBounds() {
        return m_bounds;
    } //
    
    public Rectangle2D getLayoutBounds(ItemRegistry registry) {
        if ( m_bounds != null )
            return m_bounds;
        if ( registry != null && registry.getDisplayCount() > 0 ) {
            Display d = registry.getDisplay(0);
            Insets i = d.getInsets(m_insets);
            m_bpts[0] = i.left; 
            m_bpts[1] = i.top;
            m_bpts[2] = d.getWidth()-i.right;
            m_bpts[3] = d.getHeight()-i.bottom;
            d.getInverseTransform().transform(m_bpts,0,m_bpts,0,2);
            m_tmp.setRect(m_bpts[0],m_bpts[1],
                          m_bpts[2]-m_bpts[0],
                          m_bpts[3]-m_bpts[1]);
            return m_tmp;
        } else
            return null;
    } //
    
    public void setLayoutBounds(Rectangle2D b) {
        m_bounds = b;
    } //
    
    public Point2D getLayoutAnchor() {
        return m_anchor;
    } //
    
    public Point2D getLayoutAnchor(ItemRegistry registry) {
        if ( m_anchor != null )
            return m_anchor;
        Point2D a = new Point2D.Double(0,0);
        if ( registry != null ) {
            Display d = registry.getDisplay(0);
            a.setLocation(d.getWidth()/2.0,d.getHeight()/2.0);
            d.getInverseTransform().transform(a,a);
        }
        return a;
    } //
    
    public void setLayoutAnchor(Point2D a) {
        m_anchor = a;
    } //
    
    protected void setLocation(VisualItem item, VisualItem referrer, 
            double x, double y)
    {
        if ( Double.isNaN(item.getX()) ) {
            if ( referrer != null )
                item.setLocation(referrer.getStartLocation());
            else
                item.setLocation(0,0);
        }
        item.updateLocation(x,y);
        item.setLocation(x,y);
    } //
    
    public abstract void run(ItemRegistry registry, double frac);

} // end of abstract class Layout
