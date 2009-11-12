package org.codemap.graph;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.matrix.Function;
import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.org.ggobi.plugins.ggvis.Viz;
import ch.akuhn.util.Out;

public class RunIsomapMDSOfSourceCode {

    public static void main(String[] args) {
        
        Hapax hapax = Hapax.newCorpus()
        .useTFIDF()
        .useCamelCaseScanner()
        .addFiles("../ch.akuhn.hapax", ".java")
        .addFiles("../ch.akuhn.util", ".java")
        .addFiles("../ch.deif.meander", ".java")
        .build();

        System.out.println(hapax.getIndex());
        
        double[][] dist = hapax.getIndex().documentCorrelation().asArray();
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                dist[i][j] = Function.COSINE_TO_DISSIMILARITY.apply(dist[i][j]);
            }
        }
        
        double[][] knn = new Distances(dist).kayNearestNeighbours(12);
        Out.puts("2");
        double[][] path = new AllPaths(knn).undirected().run().path;
        Out.puts("3");
        new MultidimensionalScaling()
            .dissimilarities(path)
            .iterations(1000000000)
            .threshold(1e-15)
            .listener(new Viz().open())
            .run();
        Out.puts("4");
        
        
    }
    
}
