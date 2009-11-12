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

public class Codemap {

    public Document[] documents;
    public Points locations;
    public DenseMatrix distances;
    
    public Codemap(String... foldernames) {
        CorpusBuilder builder = Hapax.newCorpus().useTFIDF().useCamelCaseScanner();
        for (String folder: foldernames) builder.addFiles(folder, ".java");
        LatentSemanticIndex lsi = builder.build().getIndex();
        documents = null;
        locations = null;
        distances = new DenseMatrix(lsi.documentCorrelation().asArray());        
    }
    
    public Codemap applyIsomap() {
        distances.apply(Function.COSINE_TO_DISSIMILARITY);
        double[][] knn = new Distances(distances.value).kayNearestNeighbours(12);
        distances = new DenseMatrix(new AllPaths(knn).undirected().run().path);
        return this;
    }
    
    public void visuallyRunMDS() {
        new MultidimensionalScaling()
            .dissimilarities(distances.value)
            .iterations(1000000000)
            .threshold(1e-15)
            .listener(new Viz().open())
            .run();
    }
    
}
