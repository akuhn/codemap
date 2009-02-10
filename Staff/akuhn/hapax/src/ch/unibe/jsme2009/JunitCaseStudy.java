package ch.unibe.jsme2009;

import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.util.Out;


public class JunitCaseStudy {

    public static final String FILENAME = 
            "../MeanderCaseStudies/junit/model.h.mse";
    
    public static void main(String[] args) {
        System.out.println(corpus());
    }

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
    
}
