package edu.berkeley.guir.prefuse.graph;

import java.util.Iterator;

/**
 * Interface representing a node in a tree structure.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface TreeNode extends Node {

    /**
     * Adds a new child to this node.
     * @param e the edge to the child
     * @return true if the child edge is added successfully, false if
     *  the edge connects to a node that is alrady a child.
     */
    public boolean addChild(Edge e);  

    /**
     * Adds a new child to this node at the specified index.
     * @param idx the index at which to add the child
     * @param e the edge to the child
     * @return true if the child edge is added successfully, false if
     *  the edge connects to a node that is alrady a child.
     */
    public boolean addChild(int idx, Edge e);

    /**
     * Returns the child at the given index
     * @param idx the index of the child
     * @return the requested child
     */
    public TreeNode getChild(int idx);

    /**
     * Returns the number of children of this node.
     * @return int the number of children
     */
    public int getChildCount();
    
    /**
     * Returns the edge to the child at the given index
     * @param idx the index of the child
     * @return the requested edge
     */
    public Edge getChildEdge(int idx);
    
    /**
     * Returns an iterator over the edges connecting this node to its children
     * @return an iterator over child edges
     */
    public Iterator getChildEdges();
    
    /**
     * Returns the index of the given edge to a child.
     * @param e the edge to retrieve the index of
     * @return the index, or -1 if the edge does not 
     *  connect to a child of this node
     */
    public int getChildIndex(Edge e);

    /**
     * Returns the index of the given child.
     * @param c the child to retrieve the index of
     * @return the index, or -1 if the given node is not 
     *  a child of this node
     */
    public int getChildIndex(TreeNode c);

    /**
     * Returns an iterator over this node's children
     * @return an iterator over child nodes
     */
    public Iterator getChildren();    

    /**
     * Returns this node's next sibiling in the ordering of their parent
     * @return the next sibling node, or null if this is the last node
     */
    public TreeNode getNextSibling();
    
    /**
     * Returns the number of descendants this node has
     * @return the number of descendants
     */
    public int getDescendantCount();
    
    /**
     * Returns this node's parent
     * @return this node's parent or null if the node has no parent
     */
    public TreeNode getParent();
    
    /**
     * Returns the edge to this node's parent
     * @return the edge to this node's parent, or null if there is no parent
     */
    public Edge getParentEdge();
    
    /**
     * Returns this node's previous sibling in the ordering of their parent
     * @return the previous sibling node, or null if this is the first node
     */
    public TreeNode getPreviousSibling();

    /**
     * Indicates if the given node is a child of this one.
     * @param c the node to check as a child
     * @return true if the node is a child, false otherwise
     */
    public boolean isChild(TreeNode c);
    
    /**
     * Indicates if the given edge connects this node to a child node
     * @param e the edge to check
     * @return true if the edge connects to a child node, false otherwise
     */
    public boolean isChildEdge(Edge e);
    
    /**
     * Inidcates if a given node is a descendant of this one.
     * Nodes are considered descendants of themselves (i.e.
     *  <tt>n.isDescendant(n)</tt> will always be true).
     * @param n the node to check as a descendant
     * @return true if the given node is a descendant, false otherwise
     */
    public boolean isDescendant(TreeNode n);

    /**
     * Indicates if a given node is a sibling of this one.
     * @param n the node to check for siblinghood
     * @return true if the node is a sibling, false otherwise
     */
    public boolean isSibling(TreeNode n);
    
    /**
     * Removes all children nodes as children of this node, but the nodes will
     *  remain connected as neighbor nodes of this one.
     */
    public void removeAllAsChildren();
    
    /**
     * Removes all children nodes, both as children and as connected neighbors. 
     */
    public void removeAllChildren();
    
    /**
     * Removes the node at the given index as a child in the tree, 
     *  but leaves it as a connected neighbor node.
     * @param idx the index of the node to remove in the list of children
     * @return the removed child node
     */
    public TreeNode removeAsChild(int idx);
    
    /**
     * Removes the given node as a child in the tree, 
     *  but leaves it as a connected neighbor node.
     * @param n the node to remove
     * @return true if the node was removed, false if the node is not a child
     */
    public boolean removeAsChild(TreeNode n);
    
    /**
     * Removes the child node at the given index, 
     *  both as a child and as a connected neighbor.
     * @param idx the index of the child to remove
     * @return the removed child node
     */
    public TreeNode removeChild(int idx);
    
    /**
     * Remove the given node as a child of this node,
     *  both as a child and as a connected neighbor.
     * @param n the child to remove
     * @return true if the node was removed, false if the node is not a child
     */
    public boolean removeChild(TreeNode n);
    
    /**
     * Remove the given child edge from this node,
     *  both as a child and as a connected neighbor.
     * @param e the child edge to remove
     * @return true if the edge was removed, false if the edge does not
     *  connect to a child of this node.
     */
    public boolean removeChildEdge(Edge e);
    
    /**
     * Remove the child edge at given index from this node,
     *  both as a child and as a connected neighbor.
     * @param idx the index of the child to remove
     * @return true if the edge was removed, false if the edge does not
     *  connect to a child of this node.
     */
    public Edge removeChildEdge(int idx);
    
    /**
     * Add the given node (which should already be a neighbor!) as a
     * child of this node in the tree structure.
     * @param idx the index at which to add the node
     * @param c the node to add as a child
     * @return true if the node is added successfully as a child, false
     *  if the node is already a child of this node.
     * @throws IllegalStateException if the node is not already a neighbor
     */
    public boolean setAsChild(int idx, TreeNode c);
    
    /**
     * Add the given node (which should already be a neighbor!) as a
     * child of this node in the tree structure.
     * @param c the node to add as a child
     * @return true if the node is added successfully as a child, false
     *  if the node is already a child of this node.
     * @throws IllegalStateException if the node is not already a neighbor
     */
    public boolean setAsChild(TreeNode c);

    /**
     * Sets the number of descendants of this node. This should <b>NOT</b> 
     *  be called by any application code!!
     * @param count the number of descendants of this node
     */
    public void setDescendantCount(int count);    
    
    /**
     * Sets the parent of this node. This should <b>NOT</b> be called by
     *  application code!! Use <tt>addChild()</tt> instead.
     * @param e the edge from this node to its new parent
     */
    public void setParentEdge(Edge e);

} // end of interface TreeNode
