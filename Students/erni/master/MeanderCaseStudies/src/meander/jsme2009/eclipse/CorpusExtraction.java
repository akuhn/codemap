package meander.jsme2009.eclipse;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class CorpusExtraction {

    public static void main(String... args) {
        
        // XXX must run with -Xmx128M for greater justice.
        
        Corpus corpus = new Corpus();
        corpus.importZipArchive("data/eclipse/eclipse-sourceBuild-srcIncluded-3.2.zip", ".java");
        System.out.println(corpus);
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus);
        System.out.println(tdm.density());
        
    }
    
}
