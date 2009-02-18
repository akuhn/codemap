package meander.jsme2009.eclipse;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.SizeOf;

public class CorpusExtraction {

    public static void main(String... args) {
        
        // XXX must run with -Xmx128M for greater justice.
        
        Corpus corpus = new Corpus();
        corpus.importZipArchivePackageWise("data/eclipse/eclipse-sourceBuild-srcIncluded-2.0.1.zip", ".java");
       // corpus.importAllZipArchivesPackageWise(new File("data/eclipse"), ".java");
        System.out.println(corpus);
        System.out.println(SizeOf.deepSizeOf(corpus));
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus);
        for(Document each: corpus.documents()) {
            each.dropTerms();
        }
        tdm.trim();
        System.out.println(SizeOf.deepSizeOf(tdm));
        //System.out.println(tdm.density());
        
    }
    
}
