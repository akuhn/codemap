package org.codemap.graph;

/** Uses Floyd–Warshall algorithm to compute all shortest paths.
 * 
 * @author Adrian Kuhn, 2009
 *
 */
public class AllPaths implements Runnable {

    private int[][] path;
    
    public AllPaths(int[][] path) {
        this.path = path;
    }

    public void run() {
       for (int k = 0; k < path.length; k++) {
           for (int i = 0; i < path.length; i++) {
               for (int j = 0; j < path.length; j++) {
                   path[i][j] = Math.min(path[i][j], path[i][k]+path[k][j]);
               }
           }
       }
    }

}