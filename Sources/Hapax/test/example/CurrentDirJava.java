package example;

import static ch.akuhn.util.Get.first;
import static ch.akuhn.util.Out.p;
import static ch.akuhn.util.Out.puts;

import java.io.File;
import java.io.FileNotFoundException;

import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {

        TermDocumentMatrix tdm = new TermDocumentMatrix();
        Importer importer = new Importer(tdm);
        importer.importAllFiles(new File("../Fame"), ".java");
        importer.importAllFiles(new File("."), ".java");

        puts(tdm);

        //puts(corpus.terms().toLowerCase().sortedCounts());

        //TermDocumentMatrix tdm = new TermDocumentMatrix();

        //tdm.addCorpus(corpus);

        puts(tdm);

        puts(tdm.density());

        tdm = tdm.rejectAndWeight();

        puts(tdm);

        puts(tdm.density());

        p(tdm.termBag().sortedCounts());

        LatentSemanticIndex lsi = tdm.createIndex();

        p(first(10, lsi.rankDocumentsByTerm("bag")));
        p(first(10, lsi.rankTermsByTerm("bag")));
        p(first(10, lsi.rankDocumentsByTerm("famix")));
        p(first(10, lsi.rankDocumentsByQuery("split string by lower- and upper-case")));

        tdm.storeOn(System.out);
        
    }

}
