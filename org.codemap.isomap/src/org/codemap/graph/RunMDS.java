package org.codemap.graph;

import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.org.ggobi.plugins.ggvis.Viz;
import ch.akuhn.util.Out;

public class RunMDS {

    public static void main(String[] args) {
        Out.puts("1");
        double[][] knn = new SwissRoll(500).asDistanceMatrix().kayNearestNeighbours(5);
        Out.puts("2");
        double[][] path = new AllPaths(knn).undirected().run().path;
        Out.puts("3");
        double[][] points = new MultidimensionalScaling()
            .dissimilarities(path)
            .iterations(1000000000)
            .threshold(1e-12)
            .listener(new Viz().open())
            .run();
        Out.puts("4");
    }
    
}
