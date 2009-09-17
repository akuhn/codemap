package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.graph.Graph;

/**
 * Implements the Fruchterman-Reingold algorithm for node layout.
 * 
 * Ported from the implementation in the <a href="http://jung.sourceforge.net/">JUNG</a> framework.
 * 
 * @author Scott White, Yan-Biao Boey, Danyel Fisher
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org</a>
 */
public class FruchtermanReingoldLayout extends Layout {

    private double forceConstant;
    private double temp;
    private int maxIter = 700;
    
    private static final double EPSILON = 0.000001D;
    private static final double ALPHA = 0.1;
    
    public FruchtermanReingoldLayout() {
        this(700);
    } //
    
    public FruchtermanReingoldLayout(int maxIter) {
        this.maxIter = maxIter;
    } //
    
    public int getMaxIterations() {
        return maxIter;
    } //
    
    public void setMaxIterations(int maxIter) {
        this.maxIter = maxIter;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        Graph g = registry.getFilteredGraph();
        Rectangle2D bounds = super.getLayoutBounds(registry);
        init(g, bounds);

        for (int curIter=0; curIter < maxIter; curIter++ ) {

            // Calculate repulsion
            for (Iterator iter = g.getNodes(); iter.hasNext();) {
                NodeItem n = (NodeItem)iter.next();
                if (n.isFixed()) continue;
                calcRepulsion(g, n);
            }

            // Calculate attraction
            for (Iterator iter = g.getEdges(); iter.hasNext();) {
                EdgeItem e = (EdgeItem) iter.next();
                calcAttraction(e);
            }

            double cumulativeChange = 0;

            for (Iterator iter = g.getNodes(); iter.hasNext();) {
                NodeItem n = (NodeItem)iter.next();
                if (n.isFixed()) continue;
                calcPositions(n,bounds);
            }

            cool(curIter);
        }
        
        finish(g);
    } //
    
    private void init(Graph g, Rectangle2D b) {
        temp = b.getWidth() / 10;
        forceConstant = 0.75 * 
        	Math.sqrt(b.getHeight()*b.getWidth()/g.getNodeCount());
        
        // initialize node positions
        Iterator nodeIter = g.getNodes();
        Random rand = new Random(42); // get a deterministic layout result
        double scaleW = ALPHA*b.getWidth()/2;
        double scaleH = ALPHA*b.getHeight()/2;
        while ( nodeIter.hasNext() ) {
            NodeItem n = (NodeItem)nodeIter.next();
            FRParams np = getParams(n);
            np.loc[0] = b.getCenterX() + rand.nextDouble()*scaleW;
            np.loc[1] = b.getCenterY() + rand.nextDouble()*scaleH;
        }
    } //
    
    private void finish(Graph g) {
        Iterator nodeIter = g.getNodes();
        while ( nodeIter.hasNext() ) {
            NodeItem n = (NodeItem)nodeIter.next();
            FRParams np = getParams(n);
            this.setLocation(n,null,np.loc[0],np.loc[1]);
        }
    } //
    
    public void calcPositions(NodeItem n, Rectangle2D b) {
        FRParams np = getParams(n);
        double deltaLength = Math.max(EPSILON,
                Math.sqrt(np.disp[0]*np.disp[0] + np.disp[1]*np.disp[1]));
        
        double xDisp = np.disp[0]/deltaLength * Math.min(deltaLength, temp);

        if (Double.isNaN(xDisp)) {
            System.err.println("Mathematical error... (calcPositions:xDisp)");
         }

        double yDisp = np.disp[1]/deltaLength * Math.min(deltaLength, temp);
        
        np.loc[0] += xDisp;
        np.loc[1] += yDisp;

        // don't let nodes leave the display
        double borderWidth = b.getWidth() / 50.0;
        double x = np.loc[0];
        if (x < b.getMinX() + borderWidth) {
            x = b.getMinX() + borderWidth + Math.random() * borderWidth * 2.0;
        } else if (x > (b.getMaxX() - borderWidth)) {
            x = b.getMaxX() - borderWidth - Math.random() * borderWidth * 2.0;
        }

        double y = np.loc[1];
        if (y < b.getMinY() + borderWidth) {
            y = b.getMinY() + borderWidth + Math.random() * borderWidth * 2.0;
        } else if (y > (b.getMaxY() - borderWidth)) {
            y = b.getMaxY() - borderWidth - Math.random() * borderWidth * 2.0;
        }

        np.loc[0] = x;
        np.loc[1] = y;
    } //

    public void calcAttraction(EdgeItem e) {
        NodeItem n1 = (NodeItem)e.getFirstNode();
        FRParams n1p = getParams(n1);
        NodeItem n2 = (NodeItem)e.getSecondNode();
        FRParams n2p = getParams(n2);
        
        double xDelta = n1p.loc[0] - n2p.loc[0];
        double yDelta = n1p.loc[1] - n2p.loc[1];

        double deltaLength = Math.max(EPSILON, 
                Math.sqrt(xDelta*xDelta + yDelta*yDelta));
        double force = (deltaLength*deltaLength) / forceConstant;

        if (Double.isNaN(force)) {
            System.err.println("Mathematical error...");
        }

        double xDisp = (xDelta/deltaLength) * force;
        double yDisp = (yDelta/deltaLength) * force;
        
        n1p.disp[0] -= xDisp; n1p.disp[1] -= yDisp;
        n2p.disp[0] += xDisp; n2p.disp[1] += yDisp;
    } //

    public void calcRepulsion(Graph g, NodeItem n1) {
        FRParams np = getParams(n1);
        np.disp[0] = 0.0; np.disp[1] = 0.0;

        for (Iterator iter2 = g.getNodes(); iter2.hasNext();) {
            NodeItem n2 = (NodeItem) iter2.next();
            FRParams n2p = getParams(n2);
            if (n2.isFixed()) continue;
            if (n1 != n2) {
                double xDelta = np.loc[0] - n2p.loc[0];
                double yDelta = np.loc[1] - n2p.loc[1];

                double deltaLength = Math.max(EPSILON, 
                        Math.sqrt(xDelta*xDelta + yDelta*yDelta));

                double force = (forceConstant*forceConstant) / deltaLength;

                if (Double.isNaN(force)) {
                    System.err.println("Mathematical error...");
                }

                np.disp[0] += (xDelta/deltaLength)*force;
                np.disp[1] += (yDelta/deltaLength)*force;
            }
        }
    } //
    
    private void cool(int curIter) {
        temp *= (1.0 - curIter / (double) maxIter);
    } //

    private FRParams getParams(VisualItem item) {
        FRParams rp = (FRParams)item.getVizAttribute("frParams");
        if ( rp == null ) {
            rp = new FRParams();
            item.setVizAttribute("frParams", rp);
        }
        return rp;
    } //
    
    public class FRParams {
        double[] loc = new double[2];
        double[] disp = new double[2];
    } //
} // end of class FruchtermanReingoldLayout
