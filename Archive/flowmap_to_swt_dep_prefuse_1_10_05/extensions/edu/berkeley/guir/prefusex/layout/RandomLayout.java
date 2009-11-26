package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.assignment.Layout;

/**
 * Performs a random layout of graph nodes within the layout's bounds.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class RandomLayout extends Layout {

    public void run(ItemRegistry registry, double frac) {
        Rectangle2D b = getLayoutBounds(registry);
        double x, y;
        double w = b.getWidth();
        double h = b.getHeight();
        Iterator nodeIter = registry.getNodeItems();
        while ( nodeIter.hasNext() ) {
            VisualItem item = (VisualItem)nodeIter.next();
            x = b.getX() + Math.random()*w;
            y = b.getY() + Math.random()*h;
            setLocation(item,null,x,y);
        }
    } //

} // end of class RandomLayout
