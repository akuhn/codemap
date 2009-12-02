package example;

import static ch.akuhn.util.Get.first;

import java.io.File;
import java.io.FileNotFoundException;

import ch.akuhn.hapax.corpus.CorpusBuilderHelper;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.akuhn.util.Out;

public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {

    	
    	
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        CorpusBuilderHelper importer = new CorpusBuilderHelper(tdm);
        importer.importAllFiles(new File("../ch.deif.meander"), ".java");
        importer.importAllFiles(new File("."), ".java");

        Out.puts(tdm);

        Out.puts(Get.head(tdm.documents()));
        Out.puts(tdm.getDocument(Get.head(tdm.documents())));
        
        Out.puts(tdm);

        Out.puts(tdm.density());

        tdm = tdm.rejectAndWeight();

        Out.puts(tdm);

        Out.puts(tdm.density());

        Out.puts(tdm.terms().sortedCounts());

        LatentSemanticIndex lsi = tdm.createIndex();

        Out.puts(first(10, lsi.rankDocumentsByTerm("bag")));
        Out.puts(first(10, lsi.rankTermsByTerm("bag")));
        Out.puts(first(10, lsi.rankDocumentsByTerm("codemap")));
        Out.puts(first(10, lsi.rankDocumentsByQuery("split string by lower- and upper-case")));
        
    }

}
