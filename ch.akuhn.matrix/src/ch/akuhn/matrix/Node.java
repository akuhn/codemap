package ch.akuhn.matrix;

/** Node in an unweighted, undirected graph.
 *
 */
public class Node {


    final int index;
    public Node[] edges;
    
    public Node(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "#"+index;
    }
    
}
