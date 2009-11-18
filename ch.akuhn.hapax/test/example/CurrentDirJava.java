package example;

import static ch.akuhn.util.Get.first;
import static ch.akuhn.util.Out.p;
import static ch.akuhn.util.Out.puts;

import java.io.File;
import java.io.FileNotFoundException;

import ch.akuhn.hapax.corpus.CorpusBuilderHelper;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;

public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {

    	
    	
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        CorpusBuilderHelper importer = new CorpusBuilderHelper(tdm);
        importer.importAllFiles(new File("../ch.deif.meander"), ".java");
        importer.importAllFiles(new File("."), ".java");

        puts(tdm);

        puts(Get.head(tdm.documents()));
        p(tdm.getDocument(Get.head(tdm.documents())));
        
        puts(tdm);

        puts(tdm.density());

        tdm = tdm.rejectAndWeight();

        puts(tdm);

        puts(tdm.density());

        p(tdm.terms().sortedCounts());

        LatentSemanticIndex lsi = tdm.createIndex();

        p(first(10, lsi.rankDocumentsByTerm("bag")));
        p(first(10, lsi.rankTermsByTerm("bag")));
        p(first(10, lsi.rankDocumentsByTerm("codemap")));
        p(first(10, lsi.rankDocumentsByQuery("split string by lower- and upper-case")));
        
    }

}