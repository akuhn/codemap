package ch.akuhn.graph;

import java.util.Arrays;

import ch.akuhn.matrix.Matrix;
import ch.akuhn.util.Out;

public class DijkstraAlgorithm2 {

    private static final int DONE = -1;
    private static final int TODO = 0; // default of qindex!

    public double[] apply(Graph g, Node source, Matrix dist) {
        int N = g.nodes.length;
        double[] costs = new double[N];
        Arrays.fill(costs, Double.POSITIVE_INFINITY);
        Node[] queue = new Node[N*2];
        int[] qindex = new int[N]; 
        int qnext = 0;
   
        queue[qnext++] = source;
        qindex[source.index] = DONE;
        costs[source.index] = 0;
        
        for (int n = 0; n < qnext; n++) {
            
            Node node = queue[n];
//            System.out.println("outer loop");
//            System.out.println("\tnode = " + node);
            if (node == null) continue;
            double cost = costs[node.index];
//            System.out.println("\tcost = " + cost);
            qindex[node.index] = DONE;
           
            for (Node each: node.edges) {
//                System.out.println("\tinner loop");
//                System.out.println("\t\teach = " + each);
                int state = qindex[each.index];
//                System.out.println("\t\tstate = " + state);
                if (state == DONE) continue;
                if (state == TODO) {
//                    System.out.println("\t\tappending");
                    qindex[each.index] = qnext;
                    if (qnext == queue.length) queue = Arrays.copyOf(queue, queue.length*2);
                    queue[qnext++] = each;
                    costs[each.index] = cost + dist.get(node.index, each.index);
                }
                else {
//                    System.out.println("\t\tmaybe replacing");
                    double c;
                    if ((c = cost + dist.get(node.index, each.index)) < costs[each.index]) {
                        queue[state] = null;
                        qindex[each.index] = qnext;
                        if (qnext == queue.length) queue = Arrays.copyOf(queue, queue.length*2);
                        queue[qnext++] = each;
                        costs[each.index] = c;
                    }
                }
//                ((Out) new Out().tab().tab()).put(costs);
//                ((Out) new Out().tab().tab()).put(queue);
            }
        }
        
        return costs;
    }
    
}
