package meander.jsme2009.junit;

import hapax.test.corpus.VersionNumber;
import meander.jsme2009.HapaxDoc;
import ch.akuhn.fame.Repository;
import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.SimpleCorpus;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.deif.meander.ContourLineAlgorithm;
import ch.deif.meander.DEMAlgorithm;
import ch.deif.meander.HillshadeAlgorithm;
import ch.deif.meander.HillshadeVisualization;
import ch.deif.meander.MDS;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.NormalizeElevationAlgorithm;
import ch.deif.meander.NormalizeLocationsAlgorithm;
import ch.deif.meander.PViewer;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSELocation;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;


public class JunitCaseStudy {

    public static final String FILENAME = 
            "mse/junit_corpus.mse";
    
    public static Tower tower() {
        Tower t = new Tower();
        t.metamodel.with(HapaxDoc.class);
        t.model.importMSE(InputSource.fromFilename(FILENAME));
        return t;
    }
    
    public static Corpus corpus() {
        Serializer ser = new Serializer();
        ser.model().importMSEFile(FILENAME);
        MSEProject project = ser.model().all(MSEProject.class).iterator().next();
        Corpus corpus = new SimpleCorpus();
        for (MSERelease version: project.releases) {
            for (MSEDocument each: version.documents) {
                assert each.name != null;
                corpus.addDocument(each.name, version.name, each.terms);
            }
        }
        return corpus;
    }
    
    public static Repository locationsRepository() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus());
        tdm = tdm.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = tdm.createIndex();
        System.out.println("Computing MDS...");
        MDS mds = MDS.fromCorrelationMatrix(i);
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project("JUnit");
        String version = "";
        int index = 0;
        for (Document each: tdm.documents) {
            if (!each.version().name().equals(version)) {
                version = each.version().name();
                ser.release(version);
            }
            ser.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each.name());
            index++;
        }
        return ser.model();
    }
    
    public static void main(String[] args) {
        
        boolean compute = !!! true;
        boolean show = !!! true;
        boolean dist = true;
        int nth = 2;
        
        if (compute) {
            Repository model = locationsRepository();
            model.exportMSEFile("mse/junit_meander(2).mse");
        }
        if (show) {
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_meander.mse");
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
