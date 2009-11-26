package org.codemap.graph;

import ch.akuhn.isomap.SwissRoll;
import ch.akuhn.matrix.DoubleMatrix;
import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.org.ggobi.plugins.ggvis.Viz;
import ch.akuhn.util.Stopwatch;

public class RunMDS {

    public static void main(String[] args) {
        Stopwatch.p();
        DoubleMatrix knn = new SwissRoll(2000).asDistanceMatrix().kayNearestNeighbours(5, 0.0);
        Stopwatch.p("KNN");
        DoubleMatrix path = knn.clone().undirected().applyAllPairsShortestPath();
        Stopwatch.p("APSP");
        double[][] points = new MultidimensionalScaling()
            .dissimilarities(path.data)
            .iterations(1000000000)
            .threshold(1e-12)
            .listener(new Viz().setEdges(knn.data).open())
            .run();
        Stopwatch.p();
    }
    
}
