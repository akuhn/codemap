package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Provides an iterator over nodes backed by an iteration of edges.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class NodeIterator implements Iterator {

    private Iterator edgeIter;
    private Node     node;
    
    public NodeIterator(Iterator edgeIterator, Node sourceNode) {
        edgeIter = edgeIterator;
        node = sourceNode;
    } //
    
    /**
     * This operation is not currently supported.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    } //

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return edgeIter.hasNext();
    } //

    /**
     * @see java.util.Iterator#next()
     */
    public Object next() {
        if ( !edgeIter.hasNext() )
            throw new NoSuchElementException();
        
        Edge e = (Edge)edgeIter.next();
        return e.getAdjacentNode(node);
    } //

} // end of class NodeIterator
