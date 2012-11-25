package edu.berkeley.guir.prefuse.render;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.util.GeometryLib;

/**
 * Default edge renderer that draws edges as lines connecting nodes. Both
 * straight and curved (Bezier) lines are supported.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultEdgeRenderer extends ShapeRenderer {
	
	public static final String EDGE_TYPE = "edgeType";
	
	protected static final double HALF_PI = Math.PI / 2;
	protected static final Polygon DEFAULT_ARROW_HEAD =
		new Polygon(new int[] {0,-4,4,0}, new int[] {0,-12,-12,0}, 4);
	
	public static final int EDGE_TYPE_LINE  = 0;
	public static final int EDGE_TYPE_CURVE = 1;
	
	public static final int ALIGNMENT_LEFT   = 0;
	public static final int ALIGNMENT_RIGHT  = 1;
	public static final int ALIGNMENT_CENTER = 2;
	public static final int ALIGNMENT_BOTTOM = 1;
	public static final int ALIGNMENT_TOP    = 0;
	
	protected Line2D       m_line  = new Line2D.Float();
	protected CubicCurve2D m_cubic = new CubicCurve2D.Float();

	protected int     m_edgeType = EDGE_TYPE_LINE;
	protected int     m_xAlign1  = ALIGNMENT_CENTER;
	protected int     m_yAlign1  = ALIGNMENT_CENTER;
	protected int     m_xAlign2  = ALIGNMENT_CENTER;
	protected int     m_yAlign2  = ALIGNMENT_CENTER;
	protected int     m_width    = 1;
	protected int     m_curWidth = 1;
	protected Point2D m_tmpPoints[]  = new Point2D[2];
	protected Point2D m_ctrlPoints[] = new Point2D[2];
	protected Point2D m_isctPoints[] = new Point2D[2];
	
	protected boolean m_directed = false;
	protected Polygon m_arrowHead = DEFAULT_ARROW_HEAD;
	protected AffineTransform m_arrowTrans = new AffineTransform();

	/**
	 * Constructor.
	 */
	public DefaultEdgeRenderer() {
		m_tmpPoints[0]  = new Point2D.Float();
		m_tmpPoints[1]  = new Point2D.Float();
		m_ctrlPoints[0] = new Point2D.Float();
		m_ctrlPoints[1] = new Point2D.Float();		
		m_isctPoints[0] = new Point2D.Float();
		m_isctPoints[1] = new Point2D.Float();		
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getRenderType()
	 */
	public int getRenderType() {
		if ( m_directed ) {
			return RENDER_TYPE_DRAW_AND_FILL;
		} else {
			return RENDER_TYPE_DRAW;
		} 
	} //
  	
  	/**
  	 * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getRawShape(edu.berkeley.guir.prefuse.VisualItem)
  	 */
	protected Shape getRawShape(VisualItem item) {
		EdgeItem   edge = (EdgeItem)item;
		VisualItem item1 = (VisualItem)edge.getFirstNode();
		VisualItem item2 = (VisualItem)edge.getSecondNode();
		
		String stype = (String)edge.getVizAttribute(EDGE_TYPE);
		int type = m_edgeType;
		if ( stype != null ) {
			try {
				type = Integer.parseInt(stype);
			} catch ( Exception e ) {}
		}
		
		getAlignedPoint(m_tmpPoints[0], item1.getRenderer().getBoundsRef(item1),
						m_xAlign1, m_yAlign1);
		getAlignedPoint(m_tmpPoints[1], item2.getRenderer().getBoundsRef(item2),
						m_xAlign2, m_yAlign2);
		double n1x = m_tmpPoints[0].getX();
		double n1y = m_tmpPoints[0].getY();
		double n2x = m_tmpPoints[1].getX();
		double n2y = m_tmpPoints[1].getY();
		m_curWidth = getLineWidth(item);
		
		switch ( type ) {
			case EDGE_TYPE_LINE:			
				m_line.setLine(n1x, n1y, n2x, n2y);
				return m_line;
			case EDGE_TYPE_CURVE:
				getCurveControlPoints(edge, m_ctrlPoints,n1x,n1y,n2x,n2y);
				m_cubic.setCurve(n1x, n1y,
								m_ctrlPoints[0].getX(), m_ctrlPoints[0].getY(),
								m_ctrlPoints[1].getX(), m_ctrlPoints[1].getY(),
								n2x,n2y);
				return m_cubic;
			default:
				throw new IllegalStateException("Unknown edge type");
		}	
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void render(Graphics2D g, VisualItem item) {
		super.render(g, item);
        EdgeItem e = (EdgeItem)item;
		if ( e.isDirected() ) {
			Point2D start = null, end = null;
			int width;
			
			String stype = (String)item.getVizAttribute(EDGE_TYPE);
			int type = m_edgeType;
			if ( stype != null ) {
				try {
					type = Integer.parseInt(stype);
				} catch ( Exception ex ) {}
			}
			switch ( type ) {
				case EDGE_TYPE_LINE:
					start = m_tmpPoints[0];
					end   = m_tmpPoints[1];
					width = m_width;
					break;
				case EDGE_TYPE_CURVE:
					start = m_ctrlPoints[1];
					end   = m_tmpPoints[1];
					width = 1;
					break;
				default:
					throw new IllegalStateException("Unknown edge type.");
			}
			VisualItem item2 = (VisualItem)e.getSecondNode();
			Rectangle2D r = item2.getBounds();
			int i = GeometryLib.intersectLineRectangle(start, end, r, m_isctPoints);
			if ( i > 0 )
				end = m_isctPoints[0];
			AffineTransform at = getArrowTrans(start, end, width);
            Shape arrowHead = at.createTransformedShape(m_arrowHead);
			g.setPaint(item.getFillColor());
			g.fill(arrowHead);
		}
	} //

	/**
	 * Returns an affine transformation that maps the arrowhead shape
	 * to the position and orientation specified by the provided
	 * line segment end points.
	 */
	protected AffineTransform getArrowTrans(Point2D p1, Point2D p2, int width) {
		m_arrowTrans.setToTranslation(p2.getX(), p2.getY());
		m_arrowTrans.rotate(-HALF_PI + 
			Math.atan2(p2.getY()-p1.getY(), p2.getX()-p1.getX()));
		if ( width > 1 ) {
			double scalar = (2.0*(width-1))/4+1;
			m_arrowTrans.scale(scalar, scalar);
		}
		return m_arrowTrans;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getTransform(edu.berkeley.guir.prefuse.VisualItem)
	 */
	protected AffineTransform getTransform(VisualItem item) {
		return null;
	} //
    
    /**
     * @see edu.berkeley.guir.prefuse.render.Renderer#locatePoint(java.awt.geom.Point2D, edu.berkeley.guir.prefuse.VisualItem)
     */
    public boolean locatePoint(Point2D p, VisualItem item) {
        Shape s = getShape(item);
        if ( s == null ) {
            return false;
        } else {
            double width = Math.max(2, getLineWidth(item));
            double halfWidth = width/2.0;
            return s.intersects(p.getX()-halfWidth,
                                p.getY()-halfWidth,
                                width,width);
        }
    } //

	/**
	 * Returns the line width to be used for this VisualItem. By default,
	 * returns the value set using the <code>setWidth</code> method.
	 * Subclasses should override this method to perform custom line
	 * width determination.
	 * @param item the VisualItem for which to determine the line width
	 * @return the desired line width, in pixels
	 */
	protected int getLineWidth(VisualItem item) {
		return m_width;
	} //
    
    /**
     * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getStroke(edu.berkeley.guir.prefuse.VisualItem)
     */
    protected BasicStroke getStroke(VisualItem item) {
        return (m_curWidth == 1 ? null : new BasicStroke(m_curWidth));
    } //

	/**
	 * Determines the control points to use for cubic (Bezier) curve edges. 
	 * Override this method to provide custom curve specifications.
	 * To reduce object initialization, the entries of the Point2D array are
	 * already initialized, so use the <tt>Point2D.setLocation()</tt> method rather than
	 * <tt>new Point2D.Double()</tt> to more efficiently set custom control points.
     * @param eitem the EdgeItem we are determining the control points for
	 * @param cp array of Point2D's (length >= 2) in which to return the control points
	 * @param x1 the x co-ordinate of the first node this edge connects to
	 * @param y1 the y co-ordinate of the first node this edge connects to
	 * @param x2 the x co-ordinate of the second node this edge connects to
	 * @param y2 the y co-ordinate of the second node this edge connects to
	 */
	protected void getCurveControlPoints(EdgeItem eitem, Point2D[] cp, 
					double x1, double y1, double x2, double y2) 
	{
		double dx = x2-x1, dy = y2-y1;		
		cp[0].setLocation(x1+2*dx/3,y1);
		cp[1].setLocation(x2-dx/8,y2-dy/8);
	} //

	/**
	 * Helper method, which calculates the top-left co-ordinate of a rectangle
	 * given the rectangle's alignment.
	 */
	protected static void getAlignedPoint(Point2D p, Rectangle2D r, int xAlign, int yAlign) {
		double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
		if ( xAlign == ALIGNMENT_CENTER ) {
			x = x+(w/2);
		} else if ( xAlign == ALIGNMENT_RIGHT ) {
			x = x+w;
		}
		if ( yAlign == ALIGNMENT_CENTER ) {
			y = y+(h/2);
		} else if ( yAlign == ALIGNMENT_BOTTOM ) {
			y = y+h;
		}
		p.setLocation(x,y);
	} //

	/**
	 * Returns the type of the drawn edge. This is either EDGE_TYPE_LINE or
	 * EDGE_TYPE_CURVE.
	 * @return the edge type
	 */
	public int getEdgeType() {
		return m_edgeType;
	} //
  	
	/**
	 * Sets the type of the drawn edge. This is either EDGE_TYPE_LINE or
	 * EDGE_TYPE_CURVE.
	 * @param type the new edge type
	 */
	public void setEdgeType(int type) {
		m_edgeType = type;
	} //
  	
  	/**
  	 * Get the horizontal aligment of the edge mount point with the first node.
  	 * @return the horizontal alignment
  	 */
	public int getHorizontalAlignment1() {
		return m_xAlign1;
	} //
	
	/**
	 * Get the vertical aligment of the edge mount point with the first node.
	 * @return the vertical alignment
	 */
	public int getVerticalAlignment1() {
		return m_yAlign1;
	} //

	/**
	 * Get the horizontal aligment of the edge mount point with the second node.
	 * @return the horizontal alignment
	 */
	public int getHorizontalAlignment2() {
		return m_xAlign2;
	} //
	
	/**
	 * Get the vertical aligment of the edge mount point with the second node.
	 * @return the vertical alignment
	 */
	public int getVerticalAlignment2() {
		return m_yAlign2;
	} //
	
	/**
	 * Set the horizontal aligment of the edge mount point with the first node.
	 * @param align the horizontal alignment
	 */
	public void setHorizontalAlignment1(int align) {
		m_xAlign1 = align;
	} //
	
	/**
	 * Set the vertical aligment of the edge mount point with the first node.
	 * @param align the vertical alignment
	 */
	public void setVerticalAlignment1(int align) {
		m_yAlign1 = align;
	} //

	/**
	 * Set the horizontal aligment of the edge mount point with the second node.
	 * @param align the horizontal alignment
	 */
	public void setHorizontalAlignment2(int align) {
		m_xAlign2 = align;
	} //
	
	/**
	 * Set the vertical aligment of the edge mount point with the second node.
	 * @param align the vertical alignment
	 */
	public void setVerticalAlignment2(int align) {
		m_yAlign2 = align;
	} //
	
	/**
	 * Sets the desired width of lines. Currently only supported by edges
	 * of type EDGE_TYPE_LINE.
	 * @param w the desired line width, in pixels
	 */
	public void setWidth(int w) {
		m_width = w;
	} //

} // end of class DefaultEdgeRenderer
