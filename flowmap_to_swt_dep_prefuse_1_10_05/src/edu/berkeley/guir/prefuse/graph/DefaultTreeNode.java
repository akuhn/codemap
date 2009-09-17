package edu.berkeley.guir.prefuse.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.guir.prefuse.collections.NodeIterator;
import edu.berkeley.guir.prefuse.collections.WrapAroundIterator;

/**
 * Represents a node in a tree. This implementation supports imposing
 * tree structures on arbitrary graphs, thus supports both a list
 * of neighbor nodes and a list of child nodes, where the child list must
 * be a subset of the neighbor list. Accordingly, this class supports both
 * normal methods for adding and removing children nodes as well as
 * <i>neighbor-invariant</i> methods, that allow parent-child relations to
 * be changed without changing the underlying neighbor edges. 
 * 
 * These functionalities are particularly useful for using tree layout methods
 * to visualize more complex graph structures.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultTreeNode extends DefaultNode implements TreeNode {

	protected List m_children;
	protected int  m_numDescendants;
    
    protected TreeNode m_parent;
	protected Edge m_parentEdge;
	
	// ========================================================================
	// == CONSTRUCTOR(S) ======================================================
	
	/**
	 * Constructor. Creates a new, empty DefaultTreeNode.
	 */
	public DefaultTreeNode() {
		m_numDescendants = 0;
        m_parentEdge = null;
		m_parent = null;
	} //
	
	/**
	 * Constructor, creates a new node, and makes it the child of the
	 * specified input node.
	 * @param edgeFromParent an edge between this node and the desired parent
	 */
	public DefaultTreeNode(Edge edgeFromParent) {
		m_numDescendants = 0;
        ((TreeNode)edgeFromParent.getAdjacentNode(this))
            .addChild(edgeFromParent);
	} //
	
    
	// ========================================================================
    // == TREE NODE METHODS ===================================================
    
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#addChild(edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public boolean addChild(Edge e) {
		int i = ( m_children == null ? 0 : m_children.size() );
		return addChild(i,e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#addChild(int, edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public boolean addChild(int idx, Edge e) {
		Node n = e.getAdjacentNode(this);
		if ( n == null || e.isDirected() || !(n instanceof TreeNode) )
			throw new IllegalArgumentException("Not a valid, connecting tree edge!");
        
		TreeNode c = (TreeNode)n;
		if ( getIndex(c) > -1 )
			throw new IllegalStateException("Node is already a neighbor.");
        if ( getChildIndex(c) > -1 )
            return false;
        
		if ( m_children == null ) // lazily allocate child list
		    m_children = new ArrayList(3);
		
		int nidx = ( idx > 0 ? getIndex(getChild(idx-1))+1 : 0 );
		super.addEdge(nidx,e);
		m_children.add(idx,e);
		
		c.addEdge(e);
		c.setParentEdge(e);
		
		int delta = 1 + c.getDescendantCount();
		for ( TreeNode p = this; p != null; p = p.getParent() )
            p.setDescendantCount(p.getDescendantCount()+delta);
        return true;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChild(int)
	 */
	public TreeNode getChild(int idx) {
		if ( m_children == null || idx < 0 || idx >= m_children.size() ) {
			throw new IndexOutOfBoundsException();
		} else {
			return (TreeNode)((Edge)m_children.get(idx)).getAdjacentNode(this);
		}
	} //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildCount()
     */
    public int getChildCount() {
        return ( m_children == null ? 0 : m_children.size() );
    } //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildEdge(int)
	 */
	public Edge getChildEdge(int idx) {
        if ( m_children == null || idx < 0 || idx >= m_children.size() ) {
            throw new IndexOutOfBoundsException();
        } else {
            return (Edge)m_children.get(idx);
        }
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildIndex(edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public int getChildIndex(Edge e) {
		return ( m_children == null ? -1 : m_children.indexOf(e) );
	} //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildIndex(edu.berkeley.guir.prefuse.graph.TreeNode)
     */
    public int getChildIndex(TreeNode c) {
        if ( m_children != null )
            for ( int i=0; i < m_children.size(); i++ ) {
                if ( c == ((Edge)m_children.get(i)).getAdjacentNode(this) )
                    return i;
            }
        return -1;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildEdges()
     */
    public Iterator getChildEdges() {
        if ( m_children == null || m_children.size() == 0 ) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            int pidx = ( m_parent == null ? 0 : 
                GraphLib.nearestIndex(this, m_parent) % m_children.size() );
            if ( pidx == 0 ) {
                return m_children.iterator();
            } else {
                return new WrapAroundIterator(m_children, pidx);
            }
        }
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildren()
	 */
	public Iterator getChildren() {
		return new NodeIterator(getChildEdges(), this);
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getDescendantCount()
	 */
	public int getDescendantCount() {
		return m_numDescendants;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getNextSibling()
	 */
	public TreeNode getNextSibling() {
		int idx = m_parent.getChildIndex(this) + 1;
        return (idx==m_parent.getChildCount() ? null : m_parent.getChild(idx));
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getParent()
	 */
	public TreeNode getParent() {
		return m_parent;
	} //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getParentEdge()
     */
    public Edge getParentEdge() {
        return m_parentEdge;
    } //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#getPreviousSibling()
	 */
	public TreeNode getPreviousSibling() {
		int idx = m_parent.getChildIndex(this);
		return (idx==0 ? null : m_parent.getChild(idx-1));		
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#isChild(edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean isChild(TreeNode n) {
		return ( getChildIndex(n) >= 0 );
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isChildEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean isChildEdge(Edge e) {
        return ( m_children==null ? false : m_children.indexOf(e) > -1 );
    } //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#isDescendant(edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean isDescendant(TreeNode n) {
		while ( n != null ) {
			if ( this == n ) { 
				return true;
			} else {
				n = n.getParent();
			}
		}
		return false;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#isSibling(edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean isSibling(TreeNode n) {
		return ( this != n && this.getParent()==n.getParent() );
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAllAsChildren()
	 */
	public void removeAllAsChildren() {
		if ( m_children == null ) { return;	}
		Iterator iter = m_children.iterator();
		while ( iter.hasNext() ) {
            TreeNode c = (TreeNode)((Edge)iter.next()).getAdjacentNode(this);
			c.setParentEdge(null);
		}
		m_children.clear();
		int delta = m_numDescendants;
		for ( TreeNode p = this; p != null; p = p.getParent() )
            p.setDescendantCount(p.getDescendantCount()-delta);
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAllChildren()
	 */
	public void removeAllChildren() {
		if ( m_children == null ) { return;	}
		Iterator iter = m_children.iterator();
		while ( iter.hasNext() ) {
			Edge e = (Edge)iter.next();
            TreeNode c = (TreeNode)e.getAdjacentNode(this);
			c.setParentEdge(null);
			c.removeNeighbor(this);
            removeEdge(e);
		}
		m_children.clear();
        int delta = m_numDescendants;
        for ( TreeNode p = this; p != null; p = p.getParent() )
            p.setDescendantCount(p.getDescendantCount()-delta);
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAsChild(edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean removeAsChild(TreeNode n) {
		return ( removeAsChild(getChildIndex(n)) != null );
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAsChild(int)
	 */
	public TreeNode removeAsChild(int idx) {
		if ( idx < 0 || idx >= getChildCount() )
			throw new IndexOutOfBoundsException();
        Edge e = (Edge)m_children.remove(idx);
		TreeNode c = (TreeNode)e.getAdjacentNode(this);
		c.setParentEdge(null);
		
		int delta = 1 + c.getDescendantCount();
        for ( TreeNode p = this; p != null; p = p.getParent() )
            p.setDescendantCount(p.getDescendantCount()-delta);
		return c;
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChild(edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean removeChild(TreeNode n) {
		return ( removeChildAndNeighbor(getChildIndex(n)) != null);	
	} //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChild(int)
	 */
	public TreeNode removeChild(int idx) {
        return removeChildAndNeighbor(idx);
	} //
    
	private TreeNode removeChildAndNeighbor(int idx) {
		super.removeEdge(super.getEdge((Node)getChild(idx)));
		
		TreeNode c = removeAsChild(idx);
		c.removeNeighbor(this);
        return c;
	}
	
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChildEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean removeChildEdge(Edge e) {
        return ( removeChildEdge(getChildIndex(e)) != null );
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChildEdge(int)
     */
    public Edge removeChildEdge(int idx) {
        if ( idx < 0 || idx >= getChildCount() )
            throw new IndexOutOfBoundsException();
        Edge e = (Edge)m_children.remove(idx);
        TreeNode c = (TreeNode)e.getAdjacentNode(this);
        c.setParentEdge(null);
        c.removeEdge(e);

        int delta = 1 + c.getDescendantCount();
        for ( TreeNode p = this; p != null; p = p.getParent() )
            p.setDescendantCount(p.getDescendantCount()-delta);
        return e;
    } //
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#setAsChild(edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean setAsChild(TreeNode c) {
		int i = ( m_children == null ? 0 : m_children.size() );
		return setAsChild(i,c);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#setAsChild(int, edu.berkeley.guir.prefuse.graph.TreeNode)
	 */
	public boolean setAsChild(int i, TreeNode c) {
		int idx;
		if ( (idx=getIndex(c)) < 0 )
			throw new IllegalStateException("Node is not already a neighbor!");
        if ( getChildIndex(c) > -1 )
            return false;
        
		int size = ( m_children == null ? 0 : m_children.size() );
		if ( i < 0 || i > size )
			throw new IndexOutOfBoundsException();
        
		if ( m_children == null )
		    m_children = new ArrayList(3);
        Edge e = getEdge(idx);
		m_children.add(i, e);
		c.setParentEdge(e);
		
		int delta = 1 + c.getDescendantCount();
		for ( TreeNode p = this; p != null; p = p.getParent() )		
            p.setDescendantCount(p.getDescendantCount()+delta);
        return true;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#setDescendantCount(int)
	 */
	public void setDescendantCount(int count) {
		m_numDescendants = count;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.TreeNode#setParentEdge(edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public void setParentEdge(Edge e) {
        m_parentEdge = e;
		m_parent = (e==null ? null : (TreeNode)e.getAdjacentNode(this));
	} //
    
} // end of class DefaultTreeNode
