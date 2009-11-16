package org.codemap.graph;

public abstract class BreadthFirst {

    private boolean[] visited;
    private Node start;

    protected Node[] path;
    protected int index;
    
    public BreadthFirst(Node node) {
        this.visited = new boolean[node.count];
        this.start = node;
    }
    
    public abstract void visit();
    
    public void run() {
        this.path = new Node[start.count];
        this.index = 0;
        visit(start);
        this.path = null;
    }

    private void visit(Node node) {
        if (visited[node.index]) return;
        visited[node.index] = true;
        path[index++] = node;
        for (Node each: node.edges) visit(each);
        path[--index] = null;
    }
    
}
