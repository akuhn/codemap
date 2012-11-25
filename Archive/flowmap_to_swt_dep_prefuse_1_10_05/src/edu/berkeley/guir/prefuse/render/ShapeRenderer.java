package edu.berkeley.guir.prefuse.render;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * <p>An abstract implementation of the Renderer interface supporting the
 * drawing of basic shapes. Subclasses should override the
 * {@link #getRawShape(edu.berkeley.guir.prefuse.VisualItem) getRawShape}
 * which return the shape to draw. Optionally, subclasses can also override the
 * {@link #getTransform(edu.berkeley.guir.prefuse.VisualItem)
 * getTransform} to apply a desired <code>AffineTransform</code>
 * to the shape.</p>
 * 
 * <p><b>NOTE:</b> For more efficient rendering, subclasses should use a
 * single shape instance in memory, and update its parameters on each call
 * to getRawShape, rather than allocating a new Shape object each time.
 * Otherwise, a new object will be allocated every time something needs to
 * be drawn. This can significantly reduce performance, especially when
 * there are many things to draw.
 * </p>
 * 
 * @version 1.0
 * @author Alan Newberger
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class ShapeRenderer implements Renderer {
	public static final int RENDER_TYPE_NONE = 0;
	public static final int RENDER_TYPE_DRAW = 1;
	public static final int RENDER_TYPE_FILL = 2;
	public static final int RENDER_TYPE_DRAW_AND_FILL = 3;

    private int m_renderType = RENDER_TYPE_DRAW_AND_FILL;
    protected AffineTransform m_transform = new AffineTransform();
    
	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void render(Graphics2D g, VisualItem item) {
		Shape shape = getShape(item);
		if (shape != null)
			drawShape(g, item, shape);
	} //
	
	/**
	 * Draws the specified shape into the provided Graphics context, using
	 * color values determined from the specified VisualItem. Can be used
	 * by subclasses in custom rendering routines. 
	 */
	protected void drawShape(Graphics2D g, VisualItem item, Shape shape) {
	    // set up colors
        Paint itemColor = item.getColor();
        Paint fillColor = item.getFillColor();
        
        // render the shape
        Stroke s = g.getStroke();
        Stroke is = getStroke(item);
        if ( is != null ) g.setStroke(is);
		switch (getRenderType(item)) {
			case RENDER_TYPE_DRAW :
				g.setPaint(itemColor);
				g.draw(shape);
				break;
			case RENDER_TYPE_FILL :
				g.setPaint(fillColor);
				g.fill(shape);
				break;
			case RENDER_TYPE_DRAW_AND_FILL :
				g.setPaint(fillColor);
				g.fill(shape);
				g.setPaint(itemColor);
				g.draw(shape);
				break;
		}
        g.setStroke(s);
	} //

	/**
	 * Returns the shape describing the boundary of an item. Shape should be in
	 * image space.
	 * @param item the item for which to get the Shape
	 */
	public Shape getShape(VisualItem item) {
		AffineTransform at = getTransform(item);
		Shape rawShape = getRawShape(item);
		return (at == null? rawShape : at.createTransformedShape(rawShape));
	} //

	/**
	 * Return a non-transformed shape for the visual representation of the
	 * item. Subclasses must implement this method.
	 * @param item the VisualItem being drawn
	 * @return the "raw", untransformed shape.
	 */
	protected abstract Shape getRawShape(VisualItem item);

    /**
     * Returns the Stroke used to draw the shapes. By default, this method
     * returns null to indicate the default stroke. Subclasses can override
     * this method to control custom stroke assignment.
     * @param item the VisualItem being drawn
     * @return the Stroke to use for drawing the item
     */
    protected BasicStroke getStroke(VisualItem item) {
        return null;
    } //
    
	/**
	 * Return the graphics space transform applied to this item's shape, if any.
     * Subclasses can implement this method, otherwise it will return null 
     * to indicate no transformation is needed.
	 * @param item the VisualItem
	 * @return the graphics space transform, or null if none
	 */
	protected AffineTransform getTransform(VisualItem item) {
        return null;
    } //

	/**
	 * Returns a value indicating if a shape is drawn by its outline, by a 
     * fill, or both. The default is to draw both.
	 * @return the rendering type
	 */
	public int getRenderType(VisualItem item) {
		return m_renderType;
	} //
    
    /**
     * Sets a value indicating if a shape is drawn by its outline, by a fill, 
     * or both. The default is to draw both.
     * @param type the new rendering type. Should be one of
     *  RENDER_TYPE_NONE, RENDER_TYPE_DRAW, RENDER_TYPE_FILL, or
     *  RENDER_TYPE_DRAW_AND_FILL.
     */
    public void setRenderType(int type) {
        if ( type < RENDER_TYPE_NONE || type > RENDER_TYPE_DRAW_AND_FILL ) {
            throw new IllegalArgumentException("Unrecognized render type.");
        }
        m_renderType = type;
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#locatePoint(java.awt.geom.Point2D, edu.berkeley.guir.prefuse.VisualItem)
	 */
	public boolean locatePoint(Point2D p, VisualItem item) {
		Shape s = getShape(item);
		return (s != null ? s.contains(p) : false);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#getBoundsRef(edu.berkeley.guir.prefuse.VisualItem)
	 */
	public Rectangle2D getBoundsRef(VisualItem item) {
		Shape s = getShape(item);
        if ( s == null ) {
            return new Rectangle(-1,-1,0,0);
        } else {
            Rectangle2D r = s.getBounds2D();
            BasicStroke st = (BasicStroke)getStroke(item);
            if ( st != null ) {
                double w = st.getLineWidth();
                double w2 = w/2.0;
                r.setFrame(r.getX()-w2,r.getY()-w2,
                    r.getWidth()+w,r.getHeight()+w);
            }
            return r;
        }
	} //

} // end of interface Renderer
