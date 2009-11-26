package edu.berkeley.guir.prefusex.force;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A quad-tree based implementation of the Barnes-Hut algorithm for
 *  efficient n-body force simulations. This can be used to create
 *  force-based layouts of graphs. All input NodeItems to the simulation
 *  are expected to have a "mass" attribute of type float, retrievable 
 *  by the call item.getVizAttribute("mass").
 * 
 * The Barnes-Hut algorithm originally appeared in <i>"A Hierarchical 
 *  O(n log n)force calculation algorithm" by J. Barnes and P. Hut, Nature, 
 *  v.324, December 1986</i>. For more details on the algorithm, see one of
 *  the following links --
 * <ul>
 *   <li><a href="http://www.cs.berkeley.edu/~demmel/cs267/lecture26/lecture26.html">James Demmel's UC Berkeley lecture notes</a>
 *   <li><a href="http://www.physics.gmu.edu/~large/lr_forces/desc/bh/bhdesc.html">Description of the Barnes-Hut algorithm</a>
 *   <li><a href="http://www.ifa.hawaii.edu/~barnes/treecode/treeguide.html">Joshua Barnes' recent implementation</a>
 * </ul>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class NBodyForce extends AbstractForce {

	/* 
	 * The labeling scheme for quadtree child nodes goes row by row.
	 *   0 | 1    0 -> top left,    1 -> top right
	 *  -------
	 *   2 | 3    2 -> bottom left, 3 -> bottom right
	 */

    private static String[] pnames = new String[] { "GravitationalConstant", 
            "MinimumDistance", "BarnesHutTheta"  };
    
    public static final float DEFAULT_GRAV_CONSTANT = -0.4f;
    public static final float DEFAULT_MIN_DISTANCE = -1f;
    public static final float DEFAULT_THETA = 0.9f;
    public static final int GRAVITATIONAL_CONST = 0;
    public static final int MIN_DISTANCE = 1;
    public static final int BARNES_HUT_THETA = 2;
    
    private float xMin, xMax, yMin, yMax;
	private QuadTreeNodeFactory factory = new QuadTreeNodeFactory();
	private QuadTreeNode root;

    /**
     * Construct a new empty NBodyForce simulation.
     */
    public NBodyForce() {
        this(DEFAULT_GRAV_CONSTANT, DEFAULT_MIN_DISTANCE, DEFAULT_THETA);
    } //
    
    public NBodyForce(float gravConstant, float minDistance, float theta) {
        params = new float[] { gravConstant, minDistance, theta };
        root = factory.getQuadTreeNode();
    } //

    public boolean isItemForce() {
        return true;
    } //
    
    protected String[] getParameterNames() {
        return pnames;
    } //
    
	/**
	 * Set the bounds of the region for which to compute the n-body simulation
	 * @param xMin the minimum x-coordinate
	 * @param yMin the minimum y-coordinate
	 * @param xMax the maximum x-coordinate
	 * @param yMax the maximum y-coordinate
	 */
	private void setBounds(int xMin, int yMin, int xMax, int yMax) {
		this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
	} //

	/**
	 * Clears the quadtree of all entries.
	 */
	public void clear() {
		clearHelper(root);
		root = factory.getQuadTreeNode();
	} //
	
	private void clearHelper(QuadTreeNode n) {
		for ( int i = 0; i < n.children.length; i++ ) {
			if ( n.children[i] != null )
				clearHelper(n.children[i]);
		}
		factory.reclaim(n);
	} //

	/**
	 * Initialize the simulation with the provided enclosing simulation. After
     * this call has been made, the simulation can be queried for the 
     * n-body force on a given item.
	 * @param fsim the enclosing ForceSimulator
	 */
	public void init(ForceSimulator fsim) {
		clear(); // clear internal state
        
        // compute and squarify bounds of quadtree
        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE;
        Iterator itemIter = fsim.getItems();
        while ( itemIter.hasNext() ) {
            ForceItem item = (ForceItem)itemIter.next();
            int x = (int)Math.round(item.location[0]);
            int y = (int)Math.round(item.location[1]);
            if ( x < x1 ) x1 = x;
            if ( y < y1 ) y1 = y;
            if ( x > x2 ) x2 = x;
            if ( y > y2 ) y2 = y;
        }
        int dx = x2-x1, dy = y2-y1;
        if ( dx > dy ) { y2 = y1 + dx; } else { x2 = x1 + dy; }
        setBounds(x1,y1,x2,y2);
        
        // insert items into quadtree
        itemIter = fsim.getItems();
        while ( itemIter.hasNext() ) {
            ForceItem item = (ForceItem)itemIter.next();
            insert(item);
        }
        
        // calculate magnitudes and centers of mass
		calcMass(root);
	} //

	/**
	 * Inserts an item into the quadtree.
	 * @param item the ForceItem to add.
	 * @throws IllegalStateException if the current location of the item is
	 *  outside the bounds of the quadtree
	 */
	public void insert(ForceItem item) {
		// insert item into the quadtrees
		insert(item, root, xMin, yMin, xMax, yMax);
	} //

	private void insert(ForceItem p, QuadTreeNode n, 
                        float x1, float y1, float x2, float y2)
    {
		// try to insert particle p at node n in the quadtree
		// by construction, each leaf will contain either 1 or 0 particles
		if ( n.hasChildren ) {
			// n contains more than 1 particle
			insertHelper(p,n,x1,y1,x2,y2);
		} else if ( n.value != null ) {
			// n contains 1 particle
            if ( isSameLocation(n.value, p) ) {
                insertHelper(p,n,x1,y1,x2,y2);
            } else {
                ForceItem v = n.value; n.value = null;
                insertHelper(v,n,x1,y1,x2,y2);
                insertHelper(p,n,x1,y1,x2,y2);
            }
		} else { 
			// n is empty, so is a leaf
			n.value = p;
		}
	} //
    
    private static boolean isSameLocation(ForceItem f1, ForceItem f2) {
        float dx = Math.abs(f1.location[0]-f2.location[0]);
        float dy = Math.abs(f1.location[1]-f2.location[1]);
        return ( dx < 0.01 && dy < 0.01 );
    } //
    
	private void insertHelper(ForceItem p, QuadTreeNode n, 
                              float x1, float y1, float x2, float y2)
    {   
		float x = p.location[0], y = p.location[1];
		float splitx = x1 + ((x2-x1)/2);
		float splity = y1 + ((y2-y1)/2);
		int i = (x>=splitx ? 1 : 0) + (y>=splity ? 2 : 0);
		// create new child node, if necessary
		if ( n.children[i] == null ) {
			n.children[i] = factory.getQuadTreeNode();
			n.hasChildren = true;
		}
		// update bounds
		if ( i==1 || i==3 ) x1 = splitx; else x2 = splitx;
		if ( i > 1 )        y1 = splity; else y2 = splity;
		// recurse 
		insert(p,n.children[i],x1,y1,x2,y2);		
	} //

	private void calcMass(QuadTreeNode n) {
		float xcom = 0, ycom = 0;
		n.mass = 0;
        if ( n.hasChildren ) {
    		for ( int i=0; i < n.children.length; i++ ) {
    			if ( n.children[i] != null ) {
    				calcMass(n.children[i]);
    				n.mass += n.children[i].mass;
    				xcom += n.children[i].mass * n.children[i].com[0];
    				ycom += n.children[i].mass * n.children[i].com[1];
    			}
    		}
        }
        if ( n.value != null ) {
            n.mass += n.value.mass;
            xcom += n.value.mass * n.value.location[0];
            ycom += n.value.mass * n.value.location[1];
        }
        n.com[0] = xcom / n.mass;
        n.com[1] = ycom / n.mass;
	} //

    /**
     * Calculates the force vector acting on the given item.
     * @param item the ForceItem for which to compute the force
     */
	public void getForce(ForceItem item) {
		forceHelper(item,root,xMin,yMin,xMax,yMax);
	} //
	
	private void forceHelper(ForceItem item, QuadTreeNode n, 
                             float x1, float y1, float x2, float y2)
	{
		float dx = n.com[0] - item.location[0];
        float dy = n.com[1] - item.location[1];
        float r  = (float)Math.sqrt(dx*dx+dy*dy);
        boolean same = false;
        if ( r == 0.0 ) {
            // if items are in the exact same place, add some noise
            dx = ((float)Math.random()-0.5f) / 50.0f;
            dy = ((float)Math.random()-0.5f) / 50.0f;
            r  = (float)Math.sqrt(dx*dx+dy*dy);
            same = true;
        }
        boolean minDist = params[MIN_DISTANCE]>0f && r>params[MIN_DISTANCE];
		
		// the Barnes-Hut approximation criteria is if the ratio of the
		// size of the quadtree box to the distance between the point and
		// the box's center of mass is beneath some threshold theta.
		if ( (!n.hasChildren && n.value != item) || 
             (!same && (x2-x1)/r < params[BARNES_HUT_THETA]) ) 
        {
            if ( minDist ) return;
			// either only 1 particle or we meet criteria
			// for Barnes-Hut approximation, so calc force
			float v = params[GRAVITATIONAL_CONST]*item.mass*n.mass 
                        / (r*r*r);
            item.force[0] += v*dx;
            item.force[1] += v*dy;
		} else if ( n.hasChildren ) {
			// recurse for more accurate calculation
			float splitx = x1 + ((x2-x1)/2);
			float splity = y1 + ((y2-y1)/2);
			for ( int i=0; i<n.children.length; i++ ) {
				if ( n.children[i] != null ) {
					forceHelper(item, n.children[i],
						(i==1||i==3?splitx:x1), (i>1?splity:y1),
						(i==1||i==3?x2:splitx), (i>1?y2:splity));
				}
			}
            if ( minDist ) return;
            if ( n.value != null && n.value != item ) {
                float v = params[GRAVITATIONAL_CONST]*item.mass*n.value.mass
                            / (r*r*r);
                item.force[0] += v*dx;
                item.force[1] += v*dy;
            }
		}
	} //

	/**
	 * Represents a node in the quadtree.
	 */
	public final class QuadTreeNode {
		public QuadTreeNode() {
            com = new float[] {0.0f, 0.0f};
			children = new QuadTreeNode[4];
		} //
		boolean hasChildren = false;
		float mass; // total mass held by this node
		float[] com; // center of mass of this node 
		ForceItem value; // ForceItem in this node, null if node has children
		QuadTreeNode[] children; // children nodes
	} // end of inner class QuadTreeNode

	/**
	 * Helper class to minimize number of object creations across multiple
	 * uses of the quadtree.
	 */
	public final class QuadTreeNodeFactory {
		private int maxNodes = 10000;
		private ArrayList nodes = new ArrayList();
		
		public QuadTreeNode getQuadTreeNode() {
			if ( nodes.size() > 0 ) {
				return (QuadTreeNode)nodes.remove(nodes.size()-1);
			} else {
				return new QuadTreeNode();
			}
		} //
		public void reclaim(QuadTreeNode n) {
			n.mass = 0;
			n.com[0] = 0.0f; n.com[1] = 0.0f;
			n.value = null;
			n.hasChildren = false;
			Arrays.fill(n.children, null);			
			if ( nodes.size() < maxNodes )
				nodes.add(n);
		} //
	} // end of inner class QuadTreeNodeFactory

} // end of class NBodyForce
