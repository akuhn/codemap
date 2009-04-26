package ch.deif.meander;

import java.io.File;
import java.util.Iterator;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.MapVisualization;

public class Meander {

    public static Meander script() {
        return new Meander();
    }

    private TermDocumentMatrix tdm;
    private Map map;
    private Layers layers;
    private boolean doneDEM = false;

    public Meander addDocuments(String folder, String... extensions) {
        if (tdm == null) tdm = new TermDocumentMatrix();
        Importer importer = new Importer(tdm);
        importer.importAllFiles(new File(folder), extensions);
        return this;
    }

    public Meander makeMap() {
        assert tdm != null : "Must call #addDocuments first.";
        assert map == null : "Cannot call #makeMap twice.";
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex lsi = tdm.createIndex();
        MDS mds = MDS.fromCorrelationMatrix(lsi);
        MapBuilder builder = Map.builder().size(512, 512);
        Iterator<Document> iterator = lsi.documents.iterator();
        for (int n = 0; n < mds.x.length; n++) {
            Document each = iterator.next();
            builder.location(mds.x[n], mds.y[n], Math.sqrt(each.termSize()), each);
        }
        builder.normalizeXY();
        map = builder.done();
        layers = new Layers(map);        
        return this;
    }

    public Meander useHillshading() {
        if (!doneDEM) {
            new DEMAlgorithm(map).run();
            new NormalizeElevationAlgorithm(map).run();
            new HillshadeAlgorithm(map).run();
            new ContourLineAlgorithm(map).run();
            doneDEM = true;
        }
        layers.useHillshading();
        return this;
    }

    public Meander add(Class<? extends MapVisualization<?>> overlay) {
        layers.add(overlay);
        return this;
    }

    public Meander openApplet() {
        layers.openApplet();
        return this;
    }

}
