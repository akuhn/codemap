package ch.akuhn.codemap;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.matrix.DenseMatrix;
import ch.akuhn.matrix.Function;
import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.org.ggobi.plugins.ggvis.Points;
import ch.akuhn.org.ggobi.plugins.ggvis.Viz;
import ch.akuhn.util.Stopwatch;

public class Codemap {

    public Document[] documents;
    public Points locations;
    public DenseMatrix distance;
    public DenseMatrix isomapDist;
    private DenseMatrix euclid;
    private double[][] knn;
    
    public Codemap(String... foldernames) {
        CorpusBuilder builder = Hapax.newCorpus().useTFIDF().useCamelCaseScanner();
        for (String folder: foldernames) builder.addFiles(folder, ".java");
        LatentSemanticIndex lsi = builder.build().getIndex();
        documents = null;
        locations = null;
        distance = new DenseMatrix(lsi.documentCorrelation().asArray());
        //euclid = new DenseMatrix(lsi.euclidianDistance().asArray());
        distance.apply(Function.COSINE_TO_DISSIMILARITY);
//        Stopwatch.p();
//        knn = new Distances(distance.value).kayNearestNeighbours(3, 0.0);
//        Stopwatch.p();
//        isomapDist = new DenseMatrix(new AllPaths(knn).undirected().run().undirected().path);
//        Stopwatch.p();
        throw new Error("should fix this script");
    }
    
    public Codemap applyEuclidianDistance() {
        distance = euclid;
        return this;
    }
    
    public Codemap applyIsomap() {
        distance = isomapDist;
        return this;
    }
    
    public void visuallyRunMDS() {
        new MultidimensionalScaling()
            .dissimilarities(distance.value)
            .iterations(1000000000)
            .threshold(1e-15)
            .listener(new Viz().setEdges(knn).open())
            .run();
    }

    public Codemap multiplyBothMetrics() {
        distance.multiplyWith(isomapDist);
        return this;
    }
    
}
