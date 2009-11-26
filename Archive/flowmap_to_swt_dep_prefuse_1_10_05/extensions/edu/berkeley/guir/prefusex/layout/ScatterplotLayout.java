package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;

/**
 * Implements a 2-D scatterplot layout.
 * TODO: this class is incomplete, still need to add support for setting axis scales.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org</a>
 */
public class ScatterplotLayout extends Layout {

    protected String xAttribute;
    protected String yAttribute;
    
    public ScatterplotLayout(String xAttr, String yAttr) {
        this.xAttribute = xAttr;
        this.yAttribute = yAttr;
    } //
    
    protected double getXCoord(VisualItem item) {
        return getCoord(item, xAttribute);
    } //
    
    protected double getYCoord(VisualItem item) {
        return getCoord(item, yAttribute);
    } //
    
    protected double getCoord(VisualItem item, String attr) {
        String value = item.getAttribute(attr);
        try {
            return Double.parseDouble(value);
        } catch ( Exception e ) {
            System.err.println("Attribute \""+attr+"\" is not a valid numerical value.");
            return Double.NaN;
        }
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        Rectangle2D b = this.getLayoutBounds(registry);
        double bx = b.getMinX(), by = b.getMinY();
        
        Iterator iter = registry.getNodeItems();
        while ( iter.hasNext() ) {
            NodeItem n = (NodeItem)iter.next();
            double x = getXCoord(n);
            double y = getYCoord(n);
            // TODO add scaling factors which corresponds to scatterplot axis values
            this.setLocation(n,null,x,y);
        }
    } //

} // end of class ScatterplotLayout