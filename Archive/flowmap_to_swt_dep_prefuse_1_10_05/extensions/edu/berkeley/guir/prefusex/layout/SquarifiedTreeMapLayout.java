package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.TreeLayout;

/**
 * <p>
 * TreeMap layout algorithm that optimizes for low aspect ratios 
 * of visualized tree nodes. TreeMaps are a form of space-filling layout
 * that represents nodes as boxes on the display, with children nodes
 * represented as boxes placed within their parent's box.
 * </p>
 * <p>
 * This particular algorithm is taken from Bruls, D.M., C. Huizing, and 
 * J.J. van Wijk, "Squarified Treemaps" In <i>Data Visualization 2000, 
 * Proceedings of the Joint Eurographics and IEEE TCVG Sumposium on 
 * Visualization</i>, 2000, pp. 33-42. Available online at:
 * <a href="http://www.win.tue.nl/~vanwijk/stm.pdf">http://www.win.tue.nl/~vanwijk/stm.pdf</a>.
 * </p>
 * <p>
 * For more information on TreeMaps in general, see 
 * <a href="http://www.cs.umd.edu/hcil/treemap-history/">http://www.cs.umd.edu/hcil/treemap-history/</a>.
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class SquarifiedTreeMapLayout extends TreeLayout {

    private static Comparator s_cmp = new Comparator() {
        public int compare(Object o1, Object o2) {
            double s1 = ((VisualItem)o1).getSize();
            double s2 = ((VisualItem)o2).getSize();
            return ( s1>s2 ? 1 : (s1<s2 ? -1 : 0));
        } //
    };
    private ArrayList m_kids = new ArrayList();
    private ArrayList m_row  = new ArrayList();
    private Rectangle2D m_r  = new Rectangle2D.Double();
    
    private double m_frame; // space between parents border and children
    
    /**
     * Creates a new SquarifiedTreeMapLayout with no spacing between
     * parent areas and their enclosed children.
     */
    public SquarifiedTreeMapLayout() {
        this(0);
    } //
    
    /**
     * Creates a new SquarifiedTreeMapLayout with the specified spacing between
     * parent areas and their enclosed children.
     * @param frame the amount of desired framing space, in pixels, between
     * parent areas and their enclosed children.
     */
    public SquarifiedTreeMapLayout(double frame) {
        m_frame = frame;
    } //
    
    /**
     * Runs the layout algorithm.
     */
    public void run(ItemRegistry registry, double frac) {
        NodeItem    root   = getLayoutRoot(registry);
        m_r.setRect(getLayoutBounds(registry));
        root.setLocation(0,0);
        Point2D d = new Point2D.Double(m_r.getWidth(), m_r.getHeight());
        root.setVizAttribute("dimension",d);
        updateArea(root, m_r);
        layout(root, m_r);
    } //

    private void layout(NodeItem p, Rectangle2D r) {
        // create sorted list of children
        Iterator childIter = p.getChildren();
        while ( childIter.hasNext() )
            m_kids.add(childIter.next());
        Collections.sort(m_kids, s_cmp);
        
        // do squarified layout of siblings
        double w = Math.min(r.getWidth(),r.getHeight());
        squarify(m_kids, m_row, w, r); 
        m_kids.clear(); // clear m_kids
        
        // recurse
        childIter = p.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            if ( c.getChildCount() > 0 ) {
                updateArea(c,r);
                layout(c, r);
            }
        }
    } //
    
    private void updateArea(NodeItem n, Rectangle2D r) {
        Point2D d = (Point2D)n.getVizAttribute("dimension");
        if ( m_frame == 0.0 ) {
            // if no framing, simply update bounding rectangle
            r.setRect(n.getX(), n.getY(),d.getX(), d.getY());
            return;
        }
        
        // compute area loss due to frame
        double dA = 2*m_frame*(d.getX()+d.getY()-2*m_frame);
        double A = n.getSize() - dA;
        
        // compute renormalization factor
        double s = 0;
        Iterator childIter = n.getChildren();
        while ( childIter.hasNext() )
            s += ((NodeItem)childIter.next()).getSize();
        double t = A/s;
        
        // re-normalize children areas
        childIter = n.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            c.setSize(c.getSize()*t);
        }
        
        // set bounding rectangle and return
        r.setRect(n.getX()+m_frame,   n.getY()+m_frame, 
                  d.getX()-2*m_frame, d.getY()-2*m_frame);
        return;
    } //
    
    private void squarify(List c, List row, double w, Rectangle2D r) {
        double worst = Double.MAX_VALUE, nworst;
        int len;
        
        while ( (len=c.size()) > 0 ) {
            row.add(c.get(len-1));
            nworst = worst(row, w);
            if ( nworst <= worst ) {
                c.remove(len-1);
                worst = nworst;
            } else {
                row.remove(row.size()-1); // remove the latest addition
                r = layoutRow(row, w, r); // layout the current row
                w = Math.min(r.getWidth(),r.getHeight()); // recompute w
                row.clear(); // clear the row
                worst = Double.MAX_VALUE;
            }
        }
        if ( row.size() > 0 ) {
            r = layoutRow(row, w, r); // layout the current row
            row.clear(); // clear the row
        }
    } //

    private double worst(List rlist, double w) {
        double rmax = Double.MIN_VALUE, rmin = Double.MAX_VALUE, s = 0.0;
        Iterator iter = rlist.iterator();
        while ( iter.hasNext() ) {
            double r = ((VisualItem)iter.next()).getSize();
            rmin = Math.min(rmin, r);
            rmax = Math.max(rmax, r);
            s += r;
        }
        s = s*s; w = w*w;
        return Math.max(w*rmax/s, s/(w*rmin));
    } //
    
    private Rectangle2D layoutRow(List row, double w, Rectangle2D r) {
        double s = 0; // sum of row areas
        Iterator rowIter = row.iterator();
        while ( rowIter.hasNext() )
            s += ((VisualItem)rowIter.next()).getSize();
        double x = r.getX(), y = r.getY(), d = 0;
        double h = s/w;
        boolean horiz = (w == r.getWidth());
        
        // set node positions and dimensions
        rowIter = row.iterator();
        while ( rowIter.hasNext() ) {
            NodeItem n = (NodeItem)rowIter.next();
            if ( horiz ) {
                setLocation(n,(NodeItem)n.getParent(),x+d,y);
            } else {
                setLocation(n,(NodeItem)n.getParent(),x,y+d);
            }
            double nw = n.getSize()/h;
            if ( horiz ) {
                setNodeDimensions(n,nw,h);
                d += nw;
            } else {
                setNodeDimensions(n,h,nw);
                d += nw;
            }
        }
        // update space available in rectangle r
        if ( horiz )
            r.setRect(x,y+h,r.getWidth(),r.getHeight()-h);
        else
            r.setRect(x+h,y,r.getWidth()-h,r.getHeight());
        return r;
    } //
    
    private void setNodeDimensions(NodeItem n, double w, double h) {
        Point2D d = (Point2D)n.getVizAttribute("dimension");
        if ( d == null ) {
            d = new Point2D.Double();
            n.setVizAttribute("dimension", d);
        }
        d.setLocation(w,h);
    } //
    
} // end of class SquarifiedTreeMapLayout
