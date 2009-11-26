package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Point2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.TreeLayout;

/**
 * <p>Calculates a BalloonTree layout of a tree. This layout places children
 * nodes radially around their parents, and is equivalent to a 2D view of
 * a ConeTree.</p>
 * 
 * <p>This is an implementation of the algorithm presented in G. Melançon and 
 * I. Herman, Circular Drawings of Rooted Trees, Reports of the Centre for 
 * Mathematics and Computer Sciences, Report Number INS–9817, 1998.
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class BalloonTreeLayout extends TreeLayout {
    
    private ItemRegistry m_registry;
    private int m_minRadius = 2;
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        m_registry = registry;
        Point2D anchor = getLayoutAnchor(registry);
        NodeItem n = getLayoutRoot(registry);
        layout(n,anchor.getX(),anchor.getY());
    } //
    
    public void layout(NodeItem n, double x, double y) {
        firstWalk(n);
        secondWalk(n,null,x,y,1,0);
    } //
    
    private void firstWalk(NodeItem n) {
        ParamBlock np = getParams(n);
        np.d = 0;
        double s = 0;
        Iterator childIter = n.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            firstWalk(c);
            ParamBlock cp = getParams(c);
            np.d = Math.max(np.d,cp.r);
            cp.a = Math.atan(((double)cp.r)/(np.d+cp.r));
            s += cp.a;
        }
        adjustChildren(np, s);
        setRadius(np);
    } //
    
    private void adjustChildren(ParamBlock np, double s) {
        if ( s > Math.PI ) {
            np.c = Math.PI/s;
            np.f = 0;
        } else {
            np.c = 1;
            np.f = Math.PI - s;
        }
    } //
    
    private void setRadius(ParamBlock np) {
        np.r = Math.max(np.d,m_minRadius) + 2*np.d;
    } //
    
    private void setRadius(NodeItem n, ParamBlock np) {
        int numChildren = n.getChildCount();
        double p  = Math.PI;
        double fs = (numChildren==0 ? 0 : np.f/numChildren);
        double pr = 0;
        double bx = 0, by = 0;
        Iterator childIter = n.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            ParamBlock cp = getParams(c);
            p += pr + cp.a + fs;
            bx += (cp.r)*Math.cos(p);
            by += (cp.r)*Math.sin(p);
            pr = cp.a;
        }
        if ( numChildren != 0 ) {
            bx /= numChildren;
            by /= numChildren;
        }
        np.rx = -bx;
        np.ry = -by;
        
        p = Math.PI;
        pr = 0;
        np.r = 0;
        childIter = n.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            ParamBlock cp = getParams(c);
            p += pr + cp.a + fs;
            double x = cp.r*Math.cos(p)-bx;
            double y = cp.r*Math.sin(p)-by;
            double d = Math.sqrt(x*x+y*y) + cp.r;
            np.r = Math.max(np.r, (int)Math.round(d));
            pr = cp.a;
        }
        if ( np.r == 0 )
            np.r = m_minRadius + 2*np.d;
    } //
    
    private void secondWalk2(NodeItem n, NodeItem r, 
            double x, double y, double l, double t)
    {
        ParamBlock np = getParams(n);
        double cost = Math.cos(t);
        double sint = Math.sin(t);
        double nx = x + l*(np.rx*cost-np.ry*sint);
        double ny = y + l*(np.rx*sint+np.ry*cost);
        setLocation(n,r,nx,ny);
        double dd = l*np.d;
        double p  = Math.PI;
        double fs = np.f / (n.getChildCount()+1);
        double pr = 0;
        Iterator childIter = n.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            ParamBlock cp = getParams(c);
            double aa = np.c * cp.a;
            double rr = np.d * Math.tan(aa)/(1-Math.tan(aa));
            p += pr + aa + fs;
            double xx = (l*rr+dd)*Math.cos(p)+np.rx;
            double yy = (l*rr+dd)*Math.sin(p)+np.ry;
            double x2 = xx*cost - yy*sint;
            double y2 = xx*sint + yy*cost;
            pr = aa;
            secondWalk2(c, n, x+x2, y+y2, l*rr/cp.r, p);
        }
    } //
    
    private void secondWalk(NodeItem n, NodeItem r,
            double x, double y, double l, double t)
    {
        setLocation(n,r,x,y);
        ParamBlock np = getParams(n);
        int numChildren = n.getChildCount();
        double dd = l*np.d;
        double p  = t + Math.PI;
        double fs = (numChildren==0 ? 0 : np.f/numChildren);
        double pr = 0;
        Iterator childIter = n.getChildren();
        while ( childIter.hasNext() ) {
            NodeItem c = (NodeItem)childIter.next();
            ParamBlock cp = getParams(c);
            double aa = np.c * cp.a;
            double rr = np.d * Math.tan(aa)/(1-Math.tan(aa));
            p += pr + aa + fs;
            double xx = (l*rr+dd)*Math.cos(p);
            double yy = (l*rr+dd)*Math.sin(p);
            pr = aa;
            secondWalk(c, n, x+xx, y+yy, l*np.c/*l*rr/cp.r*/, p);
        }
    } //
    
    private ParamBlock getParams(NodeItem n) {
        ParamBlock np = (ParamBlock)n.getVizAttribute("balloonParams");
        if ( np == null ) {
            np = new ParamBlock();
            n.setVizAttribute("balloonParams", np);
        }
        return np;
    } //
    
    public class ParamBlock {
        public int d;
        public int r;
        public double rx, ry;
        public double a;
        public double c;
        public double f;
    } //

} // end of class BalloonTreeLayout
