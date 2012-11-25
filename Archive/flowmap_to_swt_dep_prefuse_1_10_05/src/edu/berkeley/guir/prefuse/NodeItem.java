package edu.berkeley.guir.prefuse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.collections.NodeIterator;
import edu.berkeley.guir.prefuse.collections.WrapAroundIterator;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Visual representation of a node in a graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class NodeItem extends VisualItem implements TreeNode {
		
	/**
	 * Initialize this NodeItem, binding it to the given
	 * ItemRegistry and Entity.
	 * @param registry the ItemRegistry monitoring this VisualItem
	 * @param entity the Entity represented by this VisualItem
	 */	
	public void init(ItemRegistry registry, String itemClass, Entity entity) {
		if ( entity != null && !(entity instanceof Node) ) {
			throw new IllegalArgumentException("NodeItem can only represent an Entity of type Node.");				
		}
		super.init(registry, itemClass, entity);
	} //
	
    /**
     * Crear the state of this NodeItem
     * @see edu.berkeley.guir.prefuse.VisualItem#clear()
     */
	public void clear() {
		super.clear();
        removeAllNeighbors();
	} //
	
	// ========================================================================
	
	private List m_edges = new ArrayList();
	private List m_children;
	private NodeItem m_parent;
    private EdgeItem m_parentEdge;
    private int m_numDescendants;
	
    private void nodeItemCheck(Node n) {
        if ( n != null && !(n instanceof NodeItem) )
            throw new IllegalArgumentException(
            "Node must be an instance of NodeItem");
    } //
    
    private void edgeItemCheck(Edge e) {
        if ( e != null && !(e instanceof EdgeItem) )
            throw new IllegalArgumentException(
            "Edge must be an instance of EdgeItem");
    } //

   //----------------------------------------------------------------------
    
    /**
     * Adds an edge connecting this node to another node. The edge is added to
     * the end of this node's internal list of edges.
     * @param e the Edge to add
     * @return true if the edge was added, false if the edge connects to a
     *  node that is alrady a neighbor of this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#addEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean addEdge(Edge e) {
        return addEdge(m_edges.size(), e);
    } //
    
    /**
     * Adds an edge connecting this node to another node at the specified 
     * index.
     * @param idx the index at which to insert the edge
     * @param e the Edge to add
     * @return true if the edge was added, false if the edge connects to a
     *  node that is alrady a neighbor of this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#addEdge(int, edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean addEdge(int idx, Edge e) {
        edgeItemCheck(e);
        if ( e.isDirected() && this != e.getFirstNode() ) {
            throw new IllegalArgumentException("Directed edges must have the "
                    + "source as the first node in the Edge.");
        }
        Node n = e.getAdjacentNode(this);
        if ( n == null ) {
            throw new IllegalArgumentException(
            "The Edge must be incident on this Node.");
        }
        if ( isNeighbor(n) )
            return false;     
        m_edges.add(idx,e);
        return true;
    } //
    
    /**
     * Returns the edge at the specified index.
     * @param idx the index at which to retrieve the edge
     * @return the requested Edge
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdge(int)
     */
    public Edge getEdge(int idx) {
        return (Edge)m_edges.get(idx);
    } //
    
    /**
     * Returns the edge connected to the given neighbor node.
     * @param n the neighbor node for which to retrieve the edge
     * @return the requested Edge
     * @throws NoSuchElementException if the given node is not a neighbor of
     *  this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdge(edu.berkeley.guir.prefuse.graph.Node)
     */
    public Edge getEdge(Node n) {
        nodeItemCheck(n);
        for ( int i=0; i < m_edges.size(); i++ ) {
            Edge e = (Edge)m_edges.get(i);
            if ( n == e.getAdjacentNode(this) )
                return e;
        }
        throw new NoSuchElementException();
    } //
    
    /**
     * Returns the number of edges adjacent to this node.
     * @return the number of adjacent edges. This is the same as the number
     *  of neighbor nodes connected to this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdgeCount()
     */
    public int getEdgeCount() {
        return m_edges.size();
    } //
    
    /**
     * Returns an iterator over all edge items adjacent to this node.
     * @return an iterator over all adjacent edges.
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdges()
     */
    public Iterator getEdges() {
        return m_edges.iterator();
    } //
    
    /**
     * Returns the index, or position, of an incident edge. Returns -1 if the
     * input edge is not incident on this node.
     * @param e the edge to find the index of
     * @return the edge index, or -1 if this edge is not incident
     */
    public int getIndex(Edge e) {
        edgeItemCheck(e);
        return m_edges.indexOf(e);
    } //
    
    /**
     * Returns the index, or position, of a neighbor node. Returns -1 if the
     * input node is not a neighbor of this node.
     * @param n the node to find the index of
     * @return the node index, or -1 if this node is not a neighbor
     */
    public int getIndex(Node n) {
        nodeItemCheck(n);
        for ( int i=0; i < m_edges.size(); i++ ) {
            if ( n == ((Edge)m_edges.get(i)).getAdjacentNode(this) )
                return i;
        }
        return -1;
    } //
    
    /**
     * Returns the i'th neighbor of this node.
     * @param idx the index of the neighbor in the neighbor list.
     * @return the neighbor node at the specified position
     */
    public Node getNeighbor(int idx) {
        return ((Edge)m_edges.get(idx)).getAdjacentNode(this);
    } //
    
    /**
     * Returns an iterator over all neighbor nodes of this node.
     * @return an iterator over this node's neighbors.
     */
    public Iterator getNeighbors() {
        return new NodeIterator(m_edges.iterator(), this);
    } //
    
    /**
     * Indicates if a given edge is not only incident on this node
     * but stored in this node's internal list of edges.
     * @param e the edge to check for incidence
     * @return true if the edge is incident on this node and stored in this
     *  node's internal list of edges, false otherwise.
     * @see edu.berkeley.guir.prefuse.graph.Node#isIncidentEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean isIncidentEdge(Edge e) {
        edgeItemCheck(e);
        return ( m_edges.indexOf(e) > -1 );
    } //
    
    /**
     * Indicates if a given node is a neighbor of this one.
     * @param n the node to check as a neighbor
     * @return true if the node is a neighbor, false otherwise
     */
    public boolean isNeighbor(Node n) {
        nodeItemCheck(n);
        return ( getIndex(n) > -1 );
    } //
    
    /**
     * Removes all edges incident on this node.
     */
    public void removeAllNeighbors() {
        if ( m_children != null ) m_children.clear();
        m_parentEdge = null;
        m_parent = null;
        m_edges.clear();
    } //
    
    /**
     * Remove the given edge as an incident edge on this node
     * @param e the edge to remove
     * @return true if the edge was found and removed, false otherwise
     */
    public boolean removeEdge(Edge e) {
        edgeItemCheck(e);
        int idx;
        if ( e == m_parentEdge ) {
            m_parent = null;
            m_parentEdge = null;
        } else if (m_children!=null && (idx=m_children.indexOf(e))>-1) {
            m_children.remove(idx);
        }
        idx = m_edges.indexOf(e);
        return ( idx>-1 ? m_edges.remove(idx)!=null : false );
    } //
    
    /**
     * Remove the incident edge at the specified index.
     * @param idx the index at which to remove an edge
     */
    public Edge removeEdge(int idx) {
        Edge e = (Edge)m_edges.remove(idx);
        if ( e == m_parentEdge ) {
            m_parent = null;
            m_parentEdge = null;
        } else if (m_children!=null && (idx=m_children.indexOf(e))>-1) {
            m_children.remove(idx);
        }
        return e;
    } //
    
    /**
     * Remove the given node as a neighbor of this node. The edge connecting
     * the nodes is also removed.
     * @param n the node to remove
     * @return true if the node was found and removed, false otherwise
     */
    public boolean removeNeighbor(Node n) {
        nodeItemCheck(n);
        for ( int i=0; i < m_edges.size(); i++ ) {
            if ( n == ((Edge)m_edges.get(i)).getAdjacentNode(this) )
                return m_edges.remove(i) != null;
        }
        return false;
    } //

    /**
     * Remove the neighbor node at the specified index.
     * @param idx the index at which to remove a node
     */
    public Node removeNeighbor(int idx) {
        return ((Edge)removeEdge(idx)).getAdjacentNode(this);
    } //    
    
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
        edgeItemCheck(e);
        Node n = e.getAdjacentNode(this);
        if ( n == null || e.isDirected() || !(n instanceof TreeNode) )
            throw new IllegalArgumentException("Not a valid, connecting tree edge!");
        
        TreeNode c = (TreeNode)n;
        if ( getIndex(c) > -1 )
            return false;
        if ( getChildIndex(c) > -1 )
            return false;
        
        if ( m_children == null ) // lazily allocate child list
            m_children = new ArrayList(3);
        
        int nidx = ( idx > 0 ? getIndex(getChild(idx-1))+1 : 0 );
        addEdge(nidx,e);
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
        edgeItemCheck(e);
        return ( m_children == null ? -1 : m_children.indexOf(e) );
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildIndex(edu.berkeley.guir.prefuse.graph.TreeNode)
     */
    public int getChildIndex(TreeNode c) {
        nodeItemCheck(c);
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
        nodeItemCheck(n);
        return ( getChildIndex(n) >= 0 );
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isChildEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean isChildEdge(Edge e) {
        edgeItemCheck(e);
        return ( m_children==null ? false : m_children.indexOf(e) > -1 );
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isDescendant(edu.berkeley.guir.prefuse.graph.TreeNode)
     */
    public boolean isDescendant(TreeNode n) {
        nodeItemCheck(n);
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
        nodeItemCheck(n);
        return ( this != n && this.getParent()==n.getParent() );
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAllAsChildren()
     */
    public void removeAllAsChildren() {
        if ( m_children == null ) { return; }
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
        if ( m_children == null ) { return; }
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
        nodeItemCheck(n);
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
        nodeItemCheck(n);
        return ( removeChild(getChildIndex(n)) != null );   
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChild(int)
     */
    public TreeNode removeChild(int idx) {
        TreeNode c = removeAsChild(idx);
        c.removeNeighbor(this);
        return c;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChildEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean removeChildEdge(Edge e) {
        edgeItemCheck(e);
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
        nodeItemCheck(c);
        int i = ( m_children == null ? 0 : m_children.size() );
        return setAsChild(i,c);
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.TreeNode#setAsChild(int, edu.berkeley.guir.prefuse.graph.TreeNode)
     */
    public boolean setAsChild(int i, TreeNode c) {
        nodeItemCheck(c);
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
        edgeItemCheck(e);
        m_parentEdge = (EdgeItem)e;
        m_parent = (e==null ? null : (NodeItem)e.getAdjacentNode(this));
    } //
    
    public int getDepth() {
      int d = 0;
      NodeItem item = this;
      while ( (item=(NodeItem)item.getParent()) != null )
          d++;
      return d;
  } //
    
   //----------------------------------------------------------------------
//	public void removeAllNeighbors() {
//		if ( m_children != null )  m_children.clear();
//		m_neighbors.clear();
//		m_edges.clear();
//		m_parent = null;
//	} //
//	
//	public int getDepth() {
//		int d = 0;
//		NodeItem item = this;
//		while ( (item=item.getParent()) != null )
//			d++;
//		return d;
//	} //
//	
//	public boolean isSibling(NodeItem n) {
//        NodeItem p = n.getParent();
//		return ( this != n && p != null && p == n.getParent() );
//	} //
//	
//	/**
//	 * Returns the i'th neighbor of this node.
//	 * @param i the index of the neighbor in the neighbor list.
//	 * @return DefaultNode the DefaultNode at the specified position in the list of
//	 *  neighbors
//	 */
//	public NodeItem getNeighbor(int i) {
//		return (NodeItem)m_neighbors.get(i);
//	} //
//
//	/**
//	 * Indicates if a given node is a neighbor of this one.
//	 * @param n the node to check as a neighbor
//	 * @return true if the node is a neighbor, false otherwise
//	 */
//	public boolean isNeighbor(NodeItem n) {
//		return ( getNeighborIndex(n) > -1 );
//	} //
//
//	/**
//	 * Returns the index, or position, of a neighbor node. Returns -1 if the
//	 * input node is not a neighbor of this node.
//	 * @param n the node to find the index of
//	 * @return the node index, or -1 if this node is not a neighbor
//	 */
//	public int getNeighborIndex(NodeItem n) {
//		return m_neighbors.indexOf(n);
//	} //
//
//	/**
//	 * Return the total number of neighbors of this node.
//	 * @return the number of neighbors
//	 */
//	public int getEdgeCount() {
//		return m_neighbors.size();
//	} //
//
//	/**
//	 * Remove the neighbor node at the specified index.
//	 * @param i the index at which to remove a node
//	 */
//	public NodeItem removeNeighbor(int i) {
//		EdgeItem e = (EdgeItem)m_edges.remove(i);
//		return (NodeItem)m_neighbors.remove(i);
//	} //
//
//    public Iterator getNeighbors() {
//        return m_neighbors.iterator();
//    } //
//    
//	public Iterator getEdges() {
//		return m_edges.iterator();
//	} //
//	
//	public EdgeItem getEdge(NodeItem n) {
//		return (EdgeItem)m_edges.get(m_neighbors.indexOf(n));
//	} //
//	
//	public EdgeItem getEdge(int i) {
//		return (EdgeItem)m_edges.get(i);
//	} //
//	
//	public boolean isIncidentEdge(EdgeItem e) {
//		return ( m_edges.indexOf(e) > -1 );
//	} //
//	
//	public int getEdgeIndex(EdgeItem e) {
//		return m_edges.indexOf(e);
//	} //
//	
//	public boolean addEdge(EdgeItem e) {		
//		return addEdge(m_edges.size(), e);
//	} //
//	
//	public boolean addEdge(int i, EdgeItem e) {
//		NodeItem n1 = e.getFirstNode();
//		NodeItem n2 = e.getSecondNode();
//		if ( !e.isDirected() && n2 == this ) {
//			NodeItem tmp = n1; n1 = n2; n2 = tmp;
//		}
//		if ( n1 != this ) {
//			throw new IllegalArgumentException(
//			"Edge must be incident on this Node!");
//		}
//		if ( isIncidentEdge(e) || isNeighbor(n2) )
//			return false;		
//		m_edges.add(i,e);
//		m_neighbors.add(i,n2);
//        return true;
//	} //
//	
//	public boolean removeEdge(EdgeItem e) {
//		return ( removeEdge(m_edges.indexOf(e)) != null );
//	} //
//	
//	public EdgeItem removeEdge(int i) {
//		m_neighbors.remove(i);
//		return (EdgeItem)m_edges.remove(i);
//	} //
//	
//	
//	// -- tree routines -------------------------------------------------------
//	
//	public int getChildCount() {
//		return ( m_children == null ? 0 : m_children.size() );
//	} //
//	
//	public Iterator getChildren() {
//		if ( m_children != null )
//			return m_children.iterator();
//		else
//			return Collections.EMPTY_LIST.iterator();
//	} //
//	
//	public NodeItem getChild(int idx) {
//		if ( m_children == null )
//			throw new IndexOutOfBoundsException();
//		return (NodeItem)m_children.get(idx);
//	} //
//	
//	public int getChildIndex(NodeItem child) {
//		return m_children==null ? -1 : m_children.indexOf(child);
//	} //
//
//	public boolean addChild(EdgeItem e) {
//		int i = ( m_children == null ? 0 : m_children.size() );
//		return addChild(i,e);
//	} //	
//
//	/**
//	 * Inserts a new child at the specified location in this node's child list.
//	 * @param i index at which to add the child
//	 * @param e the DefaultEdge to the child
//	 */
//	public boolean addChild(int i, EdgeItem e) {
//		NodeItem n1 = e.getFirstNode();
//		NodeItem n2 = e.getSecondNode();
//		if ( e.isDirected() || !(n1 != this ^ n2 != this) )
//			throw new IllegalArgumentException("Not a valid Edge!");
//		NodeItem c = ( n1 == this ? n2 : n1 );
//		if ( getChildIndex(c) > -1 || getNeighborIndex(c) > -1 )
//			return false;
//		if ( m_children == null )
//			m_children = new ArrayList();
//		
//		int idx = ( i > 0 ? getNeighborIndex(getChild(i-1))+1 : 0 );
//		addEdge(idx,e);
//		m_children.add(i, c);
//		
//		c.addEdge(e);
//		c.setParent(this);
//        return true;
//	} //
//	
//	public void removeChild(int idx) {
//		VisualItem item = (NodeItem)m_children.remove(idx);
//		if ( item instanceof NodeItem )
//			((NodeItem)item).setParent(null);
//	} //
//	
//	public void removeAllChildren() {
//		if ( m_children == null ) return;
//		while ( m_children.size() > 0 ) {
//			NodeItem item = (NodeItem)m_children.remove(m_children.size()-1);
//			item.setParent(null);
//		}
//	} //
//	
//	public NodeItem getParent() {
//		return m_parent;
//	} //
//	
//	public void setParent(NodeItem item) {
//		m_parent = item;
//	} //
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#addChild(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean addChild(Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#addChild(int, edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean addChild(int idx, Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildEdge(int)
//     */
//    public Edge getChildEdge(int idx) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildEdges()
//     */
//    public Iterator getChildEdges() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildIndex(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public int getChildIndex(Edge e) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getChildIndex(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public int getChildIndex(TreeNode c) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getNextSibling()
//     */
//    public TreeNode getNextSibling() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getDescendantCount()
//     */
//    public int getDescendantCount() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getParentEdge()
//     */
//    public Edge getParentEdge() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#getPreviousSibling()
//     */
//    public TreeNode getPreviousSibling() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isChild(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean isChild(TreeNode c) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isChildEdge(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean isChildEdge(Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isDescendant(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean isDescendant(TreeNode n) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#isSibling(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean isSibling(TreeNode n) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAllAsChildren()
//     */
//    public void removeAllAsChildren() {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAsChild(int)
//     */
//    public TreeNode removeAsChild(int idx) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeAsChild(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean removeAsChild(TreeNode n) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChild(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean removeChild(TreeNode n) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChildEdge(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean removeChildEdge(Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#removeChildEdge(int)
//     */
//    public Edge removeChildEdge(int idx) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#setAsChild(int, edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean setAsChild(int idx, TreeNode c) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#setAsChild(edu.berkeley.guir.prefuse.graph.TreeNode)
//     */
//    public boolean setAsChild(TreeNode c) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#setDescendantCount(int)
//     */
//    public void setDescendantCount(int count) {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.TreeNode#setParentEdge(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public void setParentEdge(Edge e) {
//        // TODO Auto-generated method stub
//        
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#addEdge(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean addEdge(Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#addEdge(int, edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean addEdge(int idx, Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#getEdge(edu.berkeley.guir.prefuse.graph.Node)
//     */
//    public Edge getEdge(Node n) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#getIndex(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public int getIndex(Edge e) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#getIndex(edu.berkeley.guir.prefuse.graph.Node)
//     */
//    public int getIndex(Node n) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#isIncidentEdge(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean isIncidentEdge(Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#isNeighbor(edu.berkeley.guir.prefuse.graph.Node)
//     */
//    public boolean isNeighbor(Node n) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#removeEdge(edu.berkeley.guir.prefuse.graph.Edge)
//     */
//    public boolean removeEdge(Edge e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * @see edu.berkeley.guir.prefuse.graph.Node#removeNeighbor(edu.berkeley.guir.prefuse.graph.Node)
//     */
//    public boolean removeNeighbor(Node n) {
//        // TODO Auto-generated method stub
//        return false;
//    }
	
} // end of class NodeItem
