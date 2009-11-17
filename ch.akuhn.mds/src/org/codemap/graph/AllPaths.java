package org.codemap.graph;

import ch.akuhn.util.Out;
import ch.akuhn.util.Stopwatch;

/** Uses Floyd–Warshall algorithm to compute all shortest paths.
 * 
 * @author Adrian Kuhn, 2009
 *
 */
public class AllPaths {

    public short[][] path;
    
    public AllPaths(short[][] path) {
        this.path = path;
    }
    
    public AllPaths undirected() {
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path.length; j++) {
                path[i][j] = (short) Math.min(path[i][j], path[j][i]);
            }
        }
        for (int i = 0; i < path.length; i++) {
            path[i][i] = 0;
        }
        return this;
    }

    public AllPaths run() {
        Stopwatch.p();
        for (int k = 0; k < path.length; k++) {
            for (int i = 0; i < path.length; i++) {
                double path_i_k = path[i][k];
                // if (path_i_k > k) continue; // since all edges are weighted = 1
                for (int j = 0; j < path.length; j++) {
                    path[i][j] = (short) Math.min(path[i][j], path_i_k + path[k][j]);
                }
            }
        }
        Stopwatch.p("all pairs shortest path, n = " + path.length);
        alternative();
        return this;
    }

    private void alternative() {
        Node[] nodes = Node.fromDistanceMatrix(path);
        short[][] p = new short[path.length][];
        for (int i = 0; i < p.length; i++) {
            p[i] = new Dijkstra(nodes[i]).run();
        }
        Stopwatch.p();
//        for (int i = 0; i < p.length; i++) {
//            for (int j = 0; j < p.length; j++) {
//                if (p[i][j] == path[i][j]) continue;
//                new Out().put(p[i]);
//                new Out().put(path[i]);
//                assert false : i+" "+j;
//            }
//        }
        path = p;
    }

}