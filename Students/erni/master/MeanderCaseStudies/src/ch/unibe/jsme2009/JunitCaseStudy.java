package ch.unibe.jsme2009;

import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.MDS;


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
            //Out.puts(each.terms);
            c.put(new Document(each), each.terms);
        }
        return c;
    }
    
    public static LatentSemanticIndex index() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus());
        return tdm.rejectAndWeight().createIndex();
    }
    
    public static void main(String[] args) {
        LatentSemanticIndex i = index();
        MDS mds = MDS.fromCorrelationMatrix(i.documentCorrelation());
    }
    
}
