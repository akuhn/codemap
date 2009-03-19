package meander.jsme2009.junit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import meander.jsme2009.Serializer;
import meander.jsme2009.Serializer.MSELocation;
import meander.jsme2009.Serializer.MSEProject;
import meander.jsme2009.Serializer.MSERelease;

import ch.akuhn.fame.Repository;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.akuhn.util.Throw;
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
import ch.deif.meander.ui.PViewer;


public class JunitCaseStudy {

    public static final String TDMFILE = 
            "mse/junit.TDM";
    
    public static TermDocumentMatrix corpus()  {
        try {
            TermDocumentMatrix TDM = TermDocumentMatrix.readFrom(new Scanner(new File(TDMFILE)));
            return TDM;
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public static Repository locationsRepository() {
        TermDocumentMatrix tdm = corpus();
        tdm = tdm.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = tdm.createIndex();
        System.out.println("Computing MDS...");
        JMDS mds = JMDS.fromCorrelationMatrix(i);
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project("JUnit");
        String version = "";
        int index = 0;
        for (Document each: Get.sorted(tdm.documents())) {
            if (!each.version().equals(version)) {
                version = each.version();
                ser.release(version);
            }
            ser.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each.name());
            index++;
        }
        return ser.model();
    }
    
    public static void main(String[] args) {
        
        boolean compute = !!! true;
        boolean show = true;
        boolean dist = !!! true;
        int nth = 10;
        
        if (compute) {
            Repository model = locationsRepository();
            model.exportMSEFile("mse/junit_meander(2).mse");
        }
        if (show) {
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_meander(2).mse");
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
        if (dist) {
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_meander.mse");
            new ComputeHausdorff(ser).run();
        }
    }
    
}
