package org.codemap.graph;

import ch.akuhn.util.Stopwatch;

/** Uses Floyd–Warshall algorithm to compute all shortest paths.
 * 
 * @author Adrian Kuhn, 2009
 *
 */
public class AllPaths {

    public double[][] path;
    
    public AllPaths(double[][] path) {
        this.path = path;
    }
    
    public AllPaths undirected() {
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path.length; j++) {
                path[i][j] = Math.min(path[i][j], path[j][i]);
            }
        }
        return this;
    }

    public AllPaths run() {
        Stopwatch.p();
        for (int k = 0; k < path.length; k++) {
            for (int i = 0; i < path.length; i++) {
                for (int j = 0; j < path.length; j++) {
                    path[i][j] = Math.min(path[i][j], path[i][k] + path[k][j]);
                }
            }
        }
        Stopwatch.p();
        alternative();
        return this;
    }

    private void alternative() {
        final int[][] p = new int[path.length][path.length];
        for (int[] row: p) {
            for (int i = 0; i < row.length; i++) {
                row[i] = Integer.MAX_VALUE;
            }
        }
        Node[] nodes = Node.fromDistanceMatrix(path);
        new BreadthFirst(nodes[0]) {
            @Override
            public void visit() {
                int i = path[index].index;
                for (int n = 0; n < index; n++) {
                    int j = path[n].index;
                    p[i][j] = Math.min(p[i][j], index - n);
                }
            }
        };
        Stopwatch.p();
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path.length; j++) {
                if (p[i][j] != path[i][j]) throw new Error(
                        String.format("(%d,%d) is %d and %f", i, j, p[i][j], path[i][j]));
            }
        }
    }

}