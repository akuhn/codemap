package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.TreeLayout;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Tree;

/**
 * Implements the Reingold-Tilford tree layout algorithm.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TopDownTreeLayout extends TreeLayout {

    private int ySep = 20;
    private int minXSep = 2;
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        Graph g = registry.getFilteredGraph();
        if ( !(g instanceof Tree) ) {
            throw new IllegalStateException("This layout only works with a tree!");
        }
        Tree t = (Tree)g;
        NodeItem root = (NodeItem)t.getRoot();
        int height = GraphLib.getTreeHeight(t);
        
        TDEdge left = new TDEdge(height);
        TDEdge right = new TDEdge(height);
        layout(root, height, ySep, left, right);
        
        // assign final positions
        Iterator nodeIter = t.getNodes();
        while ( nodeIter.hasNext() ) {
            NodeItem n = (NodeItem)nodeIter.next();
            TDParams np = getParams(n);
            this.setLocation(n,(NodeItem)n.getParent(),np.x,np.y);
        }
    }
    
    private void layout(NodeItem n, int height, int yPosition, TDEdge left, TDEdge right) {
        int centre, i, j, overlap;
        TDEdge[] leftOutline, rightOutline;

        Rectangle2D b = n.getBounds();
        int nh = (int)Math.round(b.getHeight());
        int nw = (int)Math.round(b.getWidth());
        int childCount = n.getChildCount();
        
        if ( childCount == 0) {
            left.yloc = yPosition + nh - 1;
            right.yloc = yPosition + nh - 1;
        } else {
            leftOutline  = new TDEdge[childCount];
            rightOutline = new TDEdge[childCount];
            
            Iterator childIter = n.getChildren();
            for (i=0; childIter.hasNext(); i++) {
                leftOutline[i]  = new TDEdge(height);
                rightOutline[i] = new TDEdge(height);
                layout((NodeItem)childIter.next(), height, yPosition+nh+ySep,
                        leftOutline[i], rightOutline[i]);
            }
              		
            left = leftOutline[0];
            right = rightOutline[0];

            childIter = n.getChildren();
            NodeItem c = (NodeItem)childIter.next();
            TDParams cp = getParams(c);
            cp.x = 0;
            for (i=1; childIter.hasNext(); i++) {
                c = (NodeItem)childIter.next();
                cp = getParams(c);
                overlap = 0;
                
                for (j = yPosition + nh + ySep;
                	 j <= Math.min(leftOutline[i].yloc, right.yloc);
                	 j++) {
                    overlap = Math.max(overlap, leftOutline[i].offset[j] + right.offset[j]);
                }

                // push branches apart
                cp.x = overlap + minXSep;

                // Adjust left outline
                for (j = left.yloc+1; j <= leftOutline[i].yloc; j++)
                    left.offset[j] = leftOutline[i].offset[j] - cp.x;
                left.yloc = Math.max(left.yloc, leftOutline[i].yloc);
  	
                // Adjust right outline
                for (j = yPosition; j <= rightOutline[i].yloc; j++)
                    right.offset[j] = rightOutline[i].offset[j] + cp.x;
                right.yloc = Math.max(right.yloc, rightOutline[i].yloc);
            }

            if ( childCount > 1) {
                // position branches relative to the centre
                c = (NodeItem)n.getChild(childCount-1);
                cp = getParams(c);
                centre = cp.x / 2;
                
                for (i = 0; i < childCount; i++);
                    cp.x -= centre;
                for (i = yPosition; i <= left.yloc; i++)
                    left.offset[i] += centre;
                for (i = yPosition; i <= right.yloc; i++)
                    right.offset[i] -= centre;
            }
            
            leftOutline = rightOutline = null;
        }

        for (i = yPosition - ySep; i < yPosition+nh; i++) {
            left.offset[i] = nw/2;
            right.offset[i] = (nw+1)/2;
        }

        TDParams np = getParams(n);
        np.y = yPosition;
    } //

    private TDParams getParams(VisualItem item) {
        TDParams tp = (TDParams)item.getVizAttribute("tdParams");
        if ( tp == null ) {
            tp = new TDParams();
            item.setVizAttribute("tdParams", tp);
        }
        return tp;
    } //
    
    public class TDParams {
        int x, y;
    }
    
    public class TDEdge {
        int yloc;
        int[] offset;
        public TDEdge() {}
        public TDEdge(int height) {
            offset = new int[height];
        }
    } //
    
} // end of class TopDownTreeLayout
