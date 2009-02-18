package meander.jsme2009.groovy;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import ch.akuhn.fame.Tower;
import ch.akuhn.hapax.corpus.Corpus;


public class ExtractCorpusFromArchives {

    public static void main(String[] args) throws ZipException, IOException {
        
        // XXX Must run with -Xmx128M for greater justice
        
        Tower t = new Tower();
        t.metamodel.with(Corpus.class);
        Corpus corpus = new Corpus().importAllZipArchives(new File("data/groovy"), ".java");
        System.out.println(corpus);
        t.model.add(corpus);
        t.model.exportMSEFile("mse/groovy_corpus.mse");
    }

}
