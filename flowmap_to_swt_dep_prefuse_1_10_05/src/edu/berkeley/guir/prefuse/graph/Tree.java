package edu.berkeley.guir.prefuse.graph;

/**
 * An interface representing a tree structure
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Tree extends Graph {

    public TreeNode getRoot();
    
    public void setRoot(TreeNode n);
    
    public void changeRoot(TreeNode n);
    
    public int getDepth(TreeNode n);
    
    public boolean addChild(Edge e);
    
    public boolean addChild(Node parent, Node child);
    
    public boolean removeChild(Edge e);
    
} //