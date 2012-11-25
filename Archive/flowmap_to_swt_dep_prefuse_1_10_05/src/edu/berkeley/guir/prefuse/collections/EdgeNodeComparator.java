package edu.berkeley.guir.prefuse.collections;

import java.util.Comparator;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class EdgeNodeComparator implements Comparator {

    private Comparator nodecmp;
    private Node       n;
    
    public EdgeNodeComparator(Comparator c) {
        this(c,null);
    } //
    
    public EdgeNodeComparator(Comparator c, Node n) {
        nodecmp = c;
        this.n = n;
    } //
    
    public void setIgnoredNode(Node n) {
        this.n = n;
    } //
    
    public Node getIgnoredNode() {
        return n;
    } //
    
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if ( o1 instanceof Edge && o2 instanceof Edge ) {
            Node n1 = ((Edge)o1).getAdjacentNode(n);
            Node n2 = ((Edge)o2).getAdjacentNode(n);
            return nodecmp.compare(n1,n2);
        } else {
            throw new IllegalArgumentException(
                "Compared objects must be Edge instances");
        }
    } //

} // end of class EdgeNodeComparator
