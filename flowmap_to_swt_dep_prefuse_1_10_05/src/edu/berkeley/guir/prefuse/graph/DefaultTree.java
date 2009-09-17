package edu.berkeley.guir.prefuse.graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import edu.berkeley.guir.prefuse.collections.BreadthFirstTreeIterator;
import edu.berkeley.guir.prefuse.collections.TreeEdgeIterator;

/**
 * Class for representing a tree structure. A tree is an undirected graph
 * without any cycles. Furthermore, our tree implementation assumes some level
 * of orientation, distinguishing between parent and children nodes.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultTree extends AbstractGraph implements Tree {
	
	protected TreeNode m_root; 
	
	/**
	 * Constructor.
	 * @param root
	 */
	public DefaultTree(TreeNode root) {
		m_root = root;
	} //
	
	public DefaultTree() {
		m_root = null;
	} //

	/**
	 * Indicates if this graph is directed or undirected. DefaultTree
	 * imposes that the tree be an undirected graph.
     * @see edu.berkeley.guir.prefuse.graph.Graph#isDirected()
	 */
	public boolean isDirected() {
		return false;
	} //

	/**
	 * Set a new root for the tree. This root should be a node <i>already</i>
	 * contained in the tree. This method allows the parent-child 
	 * relationships to be changed as necessary without changing the actual
	 * topology of the graph.
	 * @param root the new tree root. Should be contained in this tree.
	 */
	public void changeRoot(TreeNode root) {
		if ( !contains(root) ) {
			throw new IllegalArgumentException(
				"The new root must already be in the tree");
		}
		TreeNode n = root;
		LinkedList queue = new LinkedList();
		for ( TreeNode p = n; p != null; p = p.getParent() ) {
			queue.addFirst(p);
            if ( p == m_root ) break;
		}
		Iterator iter = queue.iterator();
		TreeNode p = (TreeNode)iter.next();
		while ( iter.hasNext() ) {
			TreeNode c = (TreeNode)iter.next();
            // perform parent swap
            int cidx = p.getChildIndex(c);
            Edge e = p.getChildEdge(cidx);
            p.removeAsChild(cidx);
            p.setParentEdge(e);
            c.setAsChild(GraphLib.nearestIndex(c,p), p);		
			p = c;
		}
		m_root = root;
	} //
	
	public void setRoot(TreeNode root) {
        if ( root != m_root ) {
            TreeNode proot = m_root;
            m_root = root;
            fireNodeRemoved(proot);
            if ( root != null )
                fireNodeAdded(root);
        }
	} //
	
	/**
	 * Returns the number of nodes in the tree.
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getNodeCount()
	 */
	public int getNodeCount() {
		return ( m_root == null ? 0 : m_root.getDescendantCount() + 1 );
	} //
	
	/**
	 * Returns the number of edges in the tree. This is always the number of
	 * nodes minus one.
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getEdgeCount()
	 */
	public int getEdgeCount() {
		return Math.max(0, getNodeCount()-1);
	} //

	/**
	 * Returns a breadth-first iteration of the tree nodes.
     * Currently this is neither thread-safe nor fail-fast, so beware.
	 * @return Iterator
	 */
	public Iterator getNodes() {
		if ( m_root == null ) {
			return Collections.EMPTY_LIST.iterator();
		} else {
			return new BreadthFirstTreeIterator(m_root);
		}
	} //
	
	/**
	 * Returns the edges of the tree in breadth-first-order. Currently
     * this is neither thread-safe nor fail-fast, so beware.
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getEdges()
	 */
	public Iterator getEdges() {
		return new TreeEdgeIterator(this.getNodes());
	} //

	/**
	 * Returns the root node of the tree.
	 * @return TreeNode
	 */
	public TreeNode getRoot() {
		return m_root;
	} //
	
	/**
	 * Returns the depth of the given node in this tree
	 * Returns -1 if the node is not in this tree
	 * @return int
	 */
	public int getDepth(TreeNode n) {
		int depth = 0;
		TreeNode p = n;
		while ( p != m_root && p != null ) {
			depth++;
			p = p.getParent();
		}
		return ( p == null ? -1 : depth );
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Tree#addChild(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean addChild(Edge e) {
        TreeNode n1 = (TreeNode)e.getFirstNode();
        TreeNode n2 = (TreeNode)e.getSecondNode();
        TreeNode p  = (contains(n1) ? n1 : n2);
        TreeNode c  = (p==n1 ? n2 : n1);
        if ( e.isDirected() || !contains(p) || contains(c) )
            return false;
        
        p.addChild(e);
        fireNodeAdded(c);
        fireEdgeAdded(e);
        return true;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Tree#addChild(edu.berkeley.guir.prefuse.graph.Node, edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean addChild(Node parent, Node child) {
        return addChild(new DefaultEdge(parent, child));
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Tree#removeChild(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean removeChild(Edge e) {
        TreeNode n1 = (TreeNode)e.getFirstNode();
        TreeNode n2 = (TreeNode)e.getSecondNode();
        if ( !contains(n1) || !contains(n2) )
            return false;
        int idx;
        TreeNode p, c;
        if ( (idx=n1.getChildIndex(e)) > -1 ) {
            p = n1; c = n2;
        } else if ( (idx=n2.getChildIndex(e)) > -1 ) {
            p = n2; c = n1;
        } else {
            return false;
        }
        p.removeChild(idx);
        fireEdgeRemoved(e);
        fireNodeRemoved(c);
        return true;
    } //

    /**
     * This operation is not supported by DefaultTree. 
     * Use <tt>setRoot()</tt> or <tt>addChild()</tt> instead.
     * @throws UnsupportedOperationException upon invocation
     * @see edu.berkeley.guir.prefuse.graph.Graph#addNode(edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean addNode(Node n) {
        throw new UnsupportedOperationException("DefaultTree does not support"
                + " addNode(). Use setRoot() or addChild() instead");
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#addEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean addEdge(Edge e) {
        if ( e.isDirected() ) {
            throw new IllegalStateException(
            "Directedness of edge and graph differ");
        }
        Node n1 = (Node)e.getFirstNode();
        Node n2 = (Node)e.getSecondNode();
        if ( !contains(n1) || !contains(n2) || 
                n1.isNeighbor(n2) || n2.isNeighbor(n1) ) {
            return false;
        }
        n1.addEdge(e);
        n2.addEdge(e);
        fireEdgeAdded(e);
        return true;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#removeNode(edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean removeNode(Node n) {
        if ( !contains(n) )
            return false;
        if ( n == m_root )
            m_root = null;
        else
            ((TreeNode)n).getParent().removeChild((TreeNode)n);
        fireNodeRemoved(n);
        return true;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#removeEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean removeEdge(Edge e) {
        if ( !contains(e) )
            return false;
        TreeNode n1 = (TreeNode)e.getFirstNode();
        TreeNode n2 = (TreeNode)e.getSecondNode();
        
        if ( !e.isTreeEdge() ) {
            n1.removeEdge(e);
            n2.removeEdge(e);
            fireEdgeRemoved(e);
        } else if ( n1 == m_root || n2 == m_root ) {
            Node tmp = m_root;
            m_root = null;
            fireNodeRemoved(tmp);
        } else {
            TreeNode p = (n1.getParent() == n2? n2 : n1);
            p.removeChildEdge(e);
            fireNodeRemoved(e.getAdjacentNode(p));
        }
        return true;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#replaceNode(edu.berkeley.guir.prefuse.graph.Node, edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean replaceNode(Node prev, Node next) {
        if ( next.getEdgeCount() > 0 || !contains(prev) || contains(next) )
            return false;
        if ( !(next instanceof TreeNode) )
            throw new IllegalArgumentException("Node next must be a TreeNode");
        TreeNode tprev = (TreeNode)prev;
        TreeNode tnext = (TreeNode)next;
        
        // redirect all edges
        Iterator iter = tprev.getEdges();
        while ( iter.hasNext() ) {
            Edge e = (Edge)iter.next();
            if ( e.getFirstNode() == tprev )
                e.setFirstNode(tnext);
            else
                e.setSecondNode(tnext);
            tnext.addEdge(e);
        }
        
        // add children edges to replacement nodes
        iter = tprev.getChildEdges();
        while ( iter.hasNext() ) {
            Edge e = (Edge)iter.next();
            tnext.addChild(e);
        }
        
        ((TreeNode)prev).removeAllAsChildren();
        prev.removeAllNeighbors();
        
        if ( tprev == m_root )
            setRoot(tnext);
        fireNodeReplaced(prev, next);
        return true;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#replaceEdge(edu.berkeley.guir.prefuse.graph.Edge, edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean replaceEdge(Edge prev, Edge next) {
        boolean rv = contains(prev) && !contains(next) && !next.isDirected();
        if ( !rv ) return false;
        Node p1 = prev.getFirstNode(), p2 = prev.getSecondNode();
        Node n1 = next.getFirstNode(), n2 = next.getSecondNode();
        rv = (p1==n1 && p2==n2) || (p1==n2 && p2==n1);     
        if ( rv ) {
            int idx = p1.getIndex(prev);
            p1.removeEdge(idx);
            p1.addEdge(idx, next);
            idx = p2.getIndex(prev);
            p2.removeEdge(idx);
            p2.addEdge(idx, next);
            fireEdgeReplaced(prev, next);
            return true;
        }
        return false;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#contains(edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean contains(Node n) {
        if ( n instanceof TreeNode ) {
            for ( TreeNode p = (TreeNode)n; p != null; p = p.getParent() )
                if ( p != null && p == m_root ) return true;
        }
        return false;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#contains(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean contains(Edge e) {
        Node n = e.getFirstNode();
        return ( contains(n) && n.isIncidentEdge(e) );
    } //

} // end of class DefaultTree
