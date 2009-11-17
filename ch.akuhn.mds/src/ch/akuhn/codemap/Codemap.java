package ch.akuhn.codemap;

import org.codemap.graph.AllPaths;
import org.codemap.graph.Distances;

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
    
    public Codemap(String... foldernames) {
        CorpusBuilder builder = Hapax.newCorpus().useTFIDF().useCamelCaseScanner();
        for (String folder: foldernames) builder.addFiles(folder, ".java");
        LatentSemanticIndex lsi = builder.build().getIndex();
        documents = null;
        locations = null;
        distance = new DenseMatrix(lsi.documentCorrelation().asArray());        
        distance.apply(Function.COSINE_TO_DISSIMILARITY);
        Stopwatch.p();
        short[][] knn = new Distances(distance.value).kayNearestNeighbours(0, 0.2);
        Stopwatch.p();
        isomapDist = new DenseMatrix(new AllPaths(knn).undirected().run().undirected().path);
        Stopwatch.p();
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
            .listener(new Viz().setEdges(isomapDist.value).open())
            .run();
    }

    public Codemap multiplyBothMetrics() {
        distance.multiplyWith(isomapDist);
        return this;
    }
    
}
