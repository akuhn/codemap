package org.codemap.graph;

import java.util.*;

public class Node {

    final int count;
    final int index;
    public Node[] edges;
    
    public Node(int index, int count) {
        this.index = index;
        this.count = count;
    }
    
    public static Node[] fromDistanceMatrix(short[][] dist) {
        Node[] nodes = new Node[dist.length];
        for (int n = 0; n < nodes.length; n++) nodes[n] = new Node(n, nodes.length);
        for (int n = 0; n < nodes.length; n++) {
            List<Node> list = new ArrayList<Node>();
            for (int m = 0; m < nodes.length; m++) {
                if (dist[n][m] != 1) continue;
                list.add(nodes[m]);
            }
            nodes[n].edges = list.toArray(new Node[list.size()]);
        }
        return nodes;
    }
    
    public Iterable<Node> breadFirsth() {
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                return new BreadFirst(Node.this);
            }
        };
    }
    
    private static class BreadFirst implements Iterator<Node> {

        private boolean[] visited;
        private List<Node>[] path;

        public BreadFirst(Node start) {
             
        }

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Node next() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            
        }
        
    }
    
}
