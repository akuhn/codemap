package meander.jsme2009.junit;

import ch.akuhn.fame.Repository;
import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.deif.meander.ContourLineAlgorithm;
import ch.deif.meander.DEMAlgorithm;
import ch.deif.meander.HausdorffDistance;
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
import ch.deif.meander.Serializer.MSELocation;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;
import ch.unibe.jsme2009.HapaxDoc;


public class JunitCaseStudy {

    public static final String FILENAME = 
            "mse/junit_case.h.mse";
    
    public static Tower tower() {
        Tower t = new Tower();
        t.metamodel.with(HapaxDoc.class);
        t.model.importMSE(InputSource.fromFilename(FILENAME));
        return t;
    }
    
    public static Corpus corpus() {
        Corpus c = new Corpus();
        for (HapaxDoc each: Get.sorted(tower().model.all(HapaxDoc.class))) {
            c.add(new Document(each, each.terms));
        }
        return c;
    }
    
    public static Repository locationsRepository() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus());
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex i = tdm.createIndex();
        MDS mds = MDS.fromCorrelationMatrix(i.documentCorrelation());
        Serializer ser = new Serializer();
        ser.project("JUnit");
        String version = "";
        int index = 0;
        for (Document each: tdm.documents) {
            HapaxDoc doc = (HapaxDoc) each.handle;
            if (!doc.getVersion().equals(version)) {
                version = doc.getVersion();
                ser.release(version);
            }
            ser.location(mds.x[index], mds.y[index], Math.sqrt(doc.terms.size()), doc.name);
            index++;
        }
        return ser.model();
    }
    
    public static void main(String[] args) {
        
        boolean compute = !!!true;
        boolean show = !!!true;
        boolean dist = true;
        
        if (compute) {
            Repository model = locationsRepository();
            model.exportMSEFile("mse/junit_meander.mse");
        }
        if (show) {
            Serializer ser = new Serializer();
            ser.model().importMSEFile("mse/junit_meander.mse");
            MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
            MSERelease rel = Get.element(0, proj.releases);
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
            MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
            Map map = null;
            HausdorffDistance hausdroff = new HausdorffDistance();
            for (MSERelease rel: proj.releases) {
                MapBuilder builder = Map.builder();
                for (MSELocation each: rel.locations) {
                    builder.location(each.x, each.y, each.height);
                }
                Map each = builder.build();
                if (map != null) {
                    System.out.println("\t\t\t\t" + hausdroff.distance(map, each));
                }
                System.out.println(rel.name);
                map = each;
            }
        }
    }
    
}
