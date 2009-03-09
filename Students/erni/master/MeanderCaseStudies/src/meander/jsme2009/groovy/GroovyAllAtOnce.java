package meander.jsme2009.groovy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import meander.jsme2009.junit.ComputeHausdorff;

import ch.akuhn.fame.Repository;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.akuhn.util.Throw;
import ch.akuhn.util.query.GroupedBy;
import ch.deif.meander.ContourLineAlgorithm;
import ch.deif.meander.DEMAlgorithm;
import ch.deif.meander.HillshadeAlgorithm;
import ch.deif.meander.HillshadeVisualization;
import ch.deif.meander.JMDS;
import ch.deif.meander.MDS;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.NormalizeElevationAlgorithm;
import ch.deif.meander.NormalizeLocationsAlgorithm;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSELocation;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;
import ch.deif.meander.ui.PViewer;


public class GroovyAllAtOnce {
    
    static final String[] VERSIONS = {
            "groovy-1.0-beta-1-src.zip",
            "groovy-1.0-beta-2-src.zip",
            "groovy-1.0-beta-3-src.zip",
            "groovy-1.0-beta-4-src.zip",
            "groovy-1.0-beta-5-src.zip",
            "groovy-1.0-beta-6-src.zip",
            "groovy-1.0-beta-7-src.zip",
            "groovy-1.0-beta-8-src.zip",
            "groovy-1.0-beta-10-src.zip",
            "groovy-1.0-jsr-01-src.zip",
            "groovy-1.0-jsr-02-src.zip",
            "groovy-1.0-jsr-03-src.zip",
            "groovy-1.0-jsr-04-src.zip",
            "groovy-1.0-jsr-05-src.zip",
            "groovy-1.0-jsr-06-src.zip",
            "groovy-1.0-RC-01-src.zip",
            "groovy-1.0-src.zip",
    };

    
    public TermDocumentMatrix importTDM() {
        try {
            return TermDocumentMatrix.readFrom(new Scanner(new File(GroovyCorpus.TDMFILE_10)));
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public Repository locationsRepository() {
        TermDocumentMatrix TDM = this.importTDM();
        TDM = TDM.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = TDM.createIndex();
        System.out.println("Computing MDS...");
        JMDS mds = JMDS.fromCorrelationMatrix(i);
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project("JUnit");
        for (String version: VERSIONS) {
            ser.release(version);
            for (Document each: i.documents) {
                if (!each.version().equals(version)) continue;
                int index = i.documents.get(each);
                ser.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each.name());
            }
        }
        return ser.model();
    }
    
    public void run() {
        boolean show = true;
        boolean hausdorff = !!! true;
        int nth = 5;
        
        Repository model = locationsRepository();
        System.out.printf("# num(doc) = %d; num(rel) = %d\n", 
                model.count(MSEDocument.class),
                model.count(MSERelease.class));
        model.exportMSEFile("mse/groovy_meander.mse");
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/groovy_meander.mse");
        
        if (hausdorff) {
            new ComputeHausdorff(ser).run();                  
        }
        if (show) {
            MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
            MSERelease rel = Get.element(nth, proj.releases);
            System.out.println(rel.name);
            MapBuilder builder = Map.builder();
            for (MSELocation each: rel.locations) {
                builder.location(each.x, each.y, each.height);
            }
            Map map = builder.build();
            new NormalizeLocationsAlgorithm(map).run();
            new DEMAlgorithm(map).run();
            new NormalizeElevationAlgorithm(map).run();
            new HillshadeAlgorithm(map).run();
            new ContourLineAlgorithm(map).run();
            //MapVisualization viz = new SketchVisualization(map);
            MapVisualization viz = new HillshadeVisualization(map);
            new PViewer(viz);            
        }
  
    }
 
    public static void main(String[] args) {
        // Run with -Xmx256M for greater justice
        new GroovyAllAtOnce().run();
    }
    
}
