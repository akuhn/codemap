package ch.deif.meander.mds;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.ContourLineAlgorithm;
import ch.deif.meander.DEMAlgorithm;
import ch.deif.meander.HillshadeAlgorithm;
import ch.deif.meander.HillshadeVisualization;
import ch.deif.meander.JMDS;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.NormalizeElevationAlgorithm;
import ch.deif.meander.NormalizeLocationsAlgorithm;
import ch.deif.meander.ui.PViewer;

public class MDSTest {

    // private static final String FILENAME = "mse/groovy.TDM";
    private static final String FILENAME = "mse/jnit_new.TDM";
    private static final String DEFAULT_VERSION = "groovy-1.0-beta-5-src.zip";

    public static void main(String... args) {
        TermDocumentMatrix tdm;
        try {
            tdm = TermDocumentMatrix.readFrom(new Scanner(new File(FILENAME)));
            tdm.rejectAndWeight();
            System.out.println("Computing LSI...");
            LatentSemanticIndex i = tdm.createIndex();
            System.out.println("Computing MDS...");
            System.out.println("with " + i.documents.size() + " documents...");
            JMDS mds = JMDS.fromCorrelationMatrix(i);
            System.out.println("Done.");

            MapBuilder builder = Map.builder();
            for (Document each : i.documents) {
                if (!each.version().equals(DEFAULT_VERSION))
                    continue;
                int index = i.documents.get(each);
                builder.location(mds.x[index], mds.y[index], Math.sqrt(each
                        .termSize()));
            }
            Map map = builder.build();
            new NormalizeLocationsAlgorithm(map).run();
            new DEMAlgorithm(map).run();
            new NormalizeElevationAlgorithm(map).run();
            new HillshadeAlgorithm(map).run();
            new ContourLineAlgorithm(map).run();
            // MapVisualization viz = new SketchVisualization(map);
            MapVisualization viz = new HillshadeVisualization(map);
            new PViewer(viz);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
