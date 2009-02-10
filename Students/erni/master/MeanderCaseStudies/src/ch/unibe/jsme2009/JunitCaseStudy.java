package ch.unibe.jsme2009;

import static ch.akuhn.util.Each.withIndex;
import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Each;
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
        for (Each<Document> each: withIndex(tdm.documents())) {
            System.out.printf("%f\t%f\t%s\n", mds.x[each.index], mds.y[each.index], each.element);
        }
    }
    
}
