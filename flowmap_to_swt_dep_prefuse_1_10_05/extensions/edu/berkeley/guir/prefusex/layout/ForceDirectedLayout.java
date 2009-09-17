package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceItem;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;

/**
 * Layout algorithm that positions graph elements based on a physics
 * simulation of interacting forces (e.g., anti-gravity, spring forces,
 * drag forces, etc).
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org</a>
 */
public class ForceDirectedLayout extends Layout {

    protected ItemRegistry registry; // temp member variable
    
    private ForceSimulator m_fsim;
    private long m_lasttime = -1L;
    private long m_maxstep = 50L;
    private boolean m_runonce;
    private int m_iterations = 100;
    private boolean m_enforceBounds;
    
    public ForceDirectedLayout(boolean enforceBounds) {
        this(enforceBounds, false);
    } //
    
    public ForceDirectedLayout(boolean enforceBounds, boolean runonce) {
        m_enforceBounds = enforceBounds;
        m_runonce = runonce;
        m_fsim = new ForceSimulator();
        m_fsim.addForce(new NBodyForce());
        m_fsim.addForce(new SpringForce());
        m_fsim.addForce(new DragForce());
    } //
    
    public ForceDirectedLayout(ForceSimulator fsim, boolean enforceBounds) {
        this(fsim, enforceBounds, false);
    } //
    
    public ForceDirectedLayout(ForceSimulator fsim, 
            boolean enforceBounds, boolean runonce)
    {
        m_enforceBounds = enforceBounds;
        m_runonce = runonce;
        m_fsim = fsim;
    } //
    
    public ForceSimulator getForceSimulator() {
        return m_fsim;
    } //
    
    public void setForceSimulator(ForceSimulator fsim) {
        m_fsim = fsim;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        this.registry = registry;
        // perform different actions if this is a run-once or
        // run-continuously layout
        if ( m_runonce ) {
            Point2D anchor = getLayoutAnchor(registry);
            Iterator iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem  nitem = (NodeItem)iter.next();
                nitem.setLocation(anchor);
            }
            m_fsim.clear();
            initSimulator(registry, m_fsim);
            for ( int i = 0; i < m_iterations; i++ )
                m_fsim.runSimulator(50);
            updateNodePositions();
        } else {
            // get timestep
            if ( m_lasttime == -1 )
                m_lasttime = System.currentTimeMillis()-20;
            long time = System.currentTimeMillis();
            long timestep = Math.min(m_maxstep, time - m_lasttime);
            m_lasttime = time;
            
            // run force simulator
            m_fsim.clear();
            initSimulator(registry, m_fsim);
            m_fsim.runSimulator(timestep);
            updateNodePositions();
        }
        // clear temp member variable
        this.registry = null;
        if ( frac == 1.0 ) {
            reset(registry);
        }
    } //

    private void updateNodePositions() {
        Rectangle2D bounds = getLayoutBounds(registry);
        double x1=0, x2=0, y1=0, y2=0;
        if ( bounds != null ) {
            x1 = bounds.getMinX(); y1 = bounds.getMinY();
            x2 = bounds.getMaxX(); y2 = bounds.getMaxY();
        }
        
        // update positions
        Iterator iter = registry.getNodeItems();
        while ( iter.hasNext() ) {
            NodeItem  nitem = (NodeItem)iter.next();
            if ( nitem.isFixed() ) {
                if ( Double.isNaN(nitem.getX()) )
                    setLocation(nitem,null,0.0,0.0);
                continue;
            }
            
            ForceItem fitem = (ForceItem)nitem.getVizAttribute("forceItem");
            double x = fitem.location[0];
            double y = fitem.location[1];
            
            if ( m_enforceBounds && bounds != null) {
                if ( x > x2 ) x = x2;
                if ( x < x1 ) x = x1;
                if ( y > y2 ) y = y2;
                if ( y < y1 ) y = y1;
            }
            setLocation(nitem,null,x,y);
        }
    } //
    
    public void reset(ItemRegistry registry) {
        Iterator iter = registry.getNodeItems();
        while ( iter.hasNext() ) {
            NodeItem nitem = (NodeItem)iter.next();
            ForceItem fitem = (ForceItem)nitem.getVizAttribute("forceItem");
            if ( fitem != null ) {
                fitem.location[0] = (float)nitem.getEndLocation().getX();
                fitem.location[1] = (float)nitem.getEndLocation().getY();
                fitem.force[0]    = fitem.force[1]    = 0;
                fitem.velocity[0] = fitem.velocity[1] = 0;
            }
        }
        m_lasttime = -1L;
    } //
    
    /**
     * Loads the simulator with all relevant force items and springs.
     */
    protected void initSimulator(ItemRegistry registry, ForceSimulator fsim) {
       Iterator iter = registry.getNodeItems();
       while ( iter.hasNext() ) {
           NodeItem nitem = (NodeItem)iter.next();
           ForceItem fitem = (ForceItem)nitem.getVizAttribute("forceItem");
           if ( fitem == null ) {
               fitem = new ForceItem();
               fitem.mass = getMassValue(nitem);
               nitem.setVizAttribute("forceItem", fitem);
           }
           double x = nitem.getEndLocation().getX();
           double y = nitem.getEndLocation().getY();
           fitem.location[0] = (Double.isNaN(x) ? 0f : (float)x);
           fitem.location[1] = (Double.isNaN(y) ? 0f : (float)y);
           fsim.addItem(fitem);
       }
       iter = registry.getEdgeItems();
       while ( iter.hasNext() ) {
           EdgeItem  e  = (EdgeItem)iter.next();
           NodeItem  n1 = (NodeItem)e.getFirstNode();
           ForceItem f1 = (ForceItem)n1.getVizAttribute("forceItem");
           NodeItem  n2 = (NodeItem)e.getSecondNode();
           ForceItem f2 = (ForceItem)n2.getVizAttribute("forceItem");
           float coeff = getSpringCoefficient(e);
           float slen = getSpringLength(e);
           fsim.addSpring(f1, f2, (coeff>=0?coeff:-1.f), (slen>=0?slen:-1.f));
       }      
    } //
    
    protected float getMassValue(NodeItem n) {
        return 1.0f;
    } //
    
    protected float getSpringLength(EdgeItem e) {
        return -1.f;
    } //
    
    protected float getSpringCoefficient(EdgeItem e) {
        return -1.f;
    } //
    
} // end of class ForceDirectedLayout