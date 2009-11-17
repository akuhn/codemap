package org.codemap.graph;

import java.util.Arrays;

public class Dijkstra {

    Node source;
    
    public Dijkstra(Node node) {
        this.source = node;
    }
    
    public short[] run() {
        short[] dist = new short[source.count];
        Arrays.fill(dist, Short.MAX_VALUE);
        boolean[] done = new boolean[source.count];
        Node[] todo = new Node[source.count];
        int index = 0;
        todo[index++] = source;
        done[source.index] = true;
        dist[source.index] = 0;
        short max_d = 0;
        for (int n = 0; n < todo.length; n++) {
            Node node = todo[n];
            if (node == null) break;
            short d = (short) (dist[node.index] + 1);
            if (d > max_d) max_d = d;
            for (Node each: node.edges) {
                if (done[each.index]) continue;
                todo[index++] = each;
                dist[each.index] = d;
                done[each.index] = true;
            }
        }
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] == Short.MAX_VALUE) dist[i] = max_d;
        }
        return dist;
    }
    
}
