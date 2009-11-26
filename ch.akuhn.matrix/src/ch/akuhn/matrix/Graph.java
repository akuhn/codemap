package ch.akuhn.matrix;

import java.util.ArrayList;
import java.util.List;


/** Unweighted, undirected graph.
 * 
 * @author Adrian Kuhn
 *
 */
public class Graph {

    public Node[] nodes;
    
    public Graph(int size) {
        nodes = new Node[size];
        for (int n = 0; n < nodes.length; n++) nodes[n] = new Node(n);
    }
    
    public Graph(double[][] dist) {
        this(dist.length);
        for (int n = 0; n < nodes.length; n++) {
            List<Node> list = new ArrayList<Node>();
            for (int m = 0; m < nodes.length; m++) {
                if (Double.isInfinite(dist[n][m])) continue;
                if (Double.isNaN(dist[n][m])) continue;
                list.add(nodes[m]);
            }
            nodes[n].edges = list.toArray(new Node[list.size()]);
        }
    }
    
    public Graph(SymetricMatrix dist) {
        this(dist.columnCount());
        for (int n = 0; n < nodes.length; n++) {
            List<Node> list = new ArrayList<Node>();
            for (int m = 0; m < nodes.length; m++) {
                double d = dist.get(n,m);
                if (Double.isInfinite(d)) continue;
                if (Double.isNaN(d)) continue;
                list.add(nodes[m]);
            }
            nodes[n].edges = list.toArray(new Node[list.size()]);
        }
    }
    
    
}
