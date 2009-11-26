package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.graph.Graph;

/**
 * Layout algorithm that positions graph elements along a circle.
 *
 * Ported from the implementation in the <a href="http://jung.sourceforge.net/">JUNG</a> framework.
 * 
 * @author Masanori Harada
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org</a>
 */
public class CircleLayout extends Layout {
    
    private double m_radius; // radius of the circle layout
    
    /**
     * Default constructor. The radius of the circle layout will be computed
     * automatically based on the display size.
     */
    public CircleLayout() {
        // do nothing
    } //
    
    /**
     * Constructor. Use the specified radius for the the circle layout,
     * regardless of the display size.
     * @param radius the radius of the circle layout.
     */
    public CircleLayout(double radius) {
        m_radius = radius;
    } //
    
	public double getRadius() {
		return m_radius;
	} //

	public void setRadius(double radius) {
		m_radius = radius;
	} //
    
	public void run(ItemRegistry registry, double frac) {
	    Graph g = registry.getFilteredGraph();
	    
	    int nn = g.getNodeCount();
	    
	    Rectangle2D r = super.getLayoutBounds(registry);	
		double height = r.getHeight();
		double width = r.getWidth();
		double cx = r.getCenterX();
		double cy = r.getCenterY();

		double radius = m_radius;
		if (radius <= 0) {
			radius = 0.45 * (height < width ? height : width);
		}

		Iterator nodeIter = g.getNodes();
		for (int i=0; nodeIter.hasNext(); i++) {
		    NodeItem n = (NodeItem)nodeIter.next();
		    double angle = (2*Math.PI*i) / nn;
		    double x = Math.cos(angle)*radius + cx;
		    double y = Math.sin(angle)*radius + cy;
		    this.setLocation(n, null, x, y);
		}
	} //

} // end of class CircleLayout
