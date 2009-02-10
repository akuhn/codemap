package ch.unibe.jsme2009;

import static ch.akuhn.util.Each.withIndex;
import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Each;
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
import ch.deif.meander.SketchVisualization;


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
        for (HapaxDoc each: tower().model.all(HapaxDoc.class)) {
            c.add(new Document(each, each.terms));
        }
        return c;
    }
    
    public static void main(String[] args) {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus());
        tdm = tdm.rejectAndWeight();
        LatentSemanticIndex i = tdm.createIndex();
        MDS mds = MDS.fromCorrelationMatrix(i.documentCorrelation());
        System.out.println(mds.r0);
        System.out.println(mds.r);
        MapBuilder builder = Map.builder().size(512, 512);
        for (Each<Document> each: withIndex(tdm.documents)) {
            //System.out.printf("%f\t%f\t%s\n", mds.x[each.index], mds.y[each.index], each.element);
        	String name = each.element.toString();
        	if (name.endsWith(("(junit3.4.zip)"))) {
        		builder.location(mds.x[each.index], mds.y[each.index], Math.sqrt(each.element.terms.size()));        		
        	}
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
