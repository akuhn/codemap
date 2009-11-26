package edu.berkeley.guir.prefuse.render;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * The NullRenderer does nothing, causing an item to be rendered "into
 * the void". Maybe used for items that must exist and have a spatial
 * location but should otherwise be invisible and non-interactive (e.g.,
 * invisible end-points for visible edges).
 *  
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class NullRenderer implements Renderer {

    Rectangle2D r = new Rectangle2D.Double(-1,-1,0,0);
    public void render(Graphics2D g, VisualItem item) {
        // the null renderer!
    } //
    
    public boolean locatePoint(Point2D p, VisualItem item) {
        return false;
    } //
    
    public Rectangle2D getBoundsRef(VisualItem item) {
        r.setRect(item.getX(), item.getY(),0,0);
        return r;
    } //

} // end of class NullRenderer
