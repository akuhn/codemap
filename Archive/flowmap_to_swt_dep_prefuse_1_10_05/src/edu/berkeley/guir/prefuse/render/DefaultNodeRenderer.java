package edu.berkeley.guir.prefuse.render;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import edu.berkeley.guir.prefuse.VisualItem;

/**
 * A default implementation of a node renderer that draws itself as a circle.
 * 
 * @author alann
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultNodeRenderer extends ShapeRenderer {

	private int m_radius = 5;
	private Ellipse2D m_circle =
		new Ellipse2D.Double(0, 0, 2 * m_radius, 2 * m_radius);

    /**
     * Creates a new DefaultNodeRenderer with default base
     * radius (5 pixels).
     */
    public DefaultNodeRenderer() {
    } //
    
    /**
     * Creates a new DefaultNodeRenderer with given base radius.
     * @param r the base radius for node circles
     */
    public DefaultNodeRenderer(int r) {
       setRadius(r);
    } //
    
    /**
     * Sets the radius of the circle drawn to represent a node.
     * @param r the radius value to set
     */
    public void setRadius(int r) {
        m_radius = r;
        m_circle.setFrameFromCenter(0,0,r,r);
    } //
    
    /**
     * Gets the radius of the circle drawn to represent a node.
     * @return the radius value
     */
    public int getRadius() {
        return m_radius;
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getRawShape(edu.berkeley.guir.prefuse.VisualItem)
	 */
	protected Shape getRawShape(VisualItem item) {
        double r = m_radius * item.getSize();
        double x = item.getX(), y = item.getY();
        if ( Double.isNaN(x) ) x = 0.0;
        if ( Double.isNaN(y) ) y = 0.0;
        m_circle.setFrameFromCenter(x,y,x+r,y+r);
		return m_circle;
	} //

} // end of class DefaultNodeRenderer
