package ch.akuhn.isomap.beta;

import java.util.Arrays;

public class DijkstrasAlgorithm {

    public static int[] apply(Graph g, int source) {
        int[] dist = new int[g.nodes.length];
        boolean[] done = new boolean[g.nodes.length];
        Node[] todo = new Node[g.nodes.length];
        Arrays.fill(dist, Integer.MAX_VALUE);
        int index = 0;
        todo[index++] = g.nodes[source];
        done[source] = true;
        dist[source] = 0;
        for (int n = 0; n < todo.length; n++) {
            Node node = todo[n];
            if (node == null) break;
            int d = dist[node.index] + 1;
            for (Node each: node.edges) {
                if (done[each.index]) continue;
                todo[index++] = each;
                dist[each.index] = d;
                done[each.index] = true;
            }
        }
        return dist;
    }

    public static int[][] apply(Graph g) {
        int[][] dist = new int[g.nodes.length][];
        for (int i = 0; i < dist.length; i++) dist[i] = apply(g, i);
        return dist;
    }
    
}
