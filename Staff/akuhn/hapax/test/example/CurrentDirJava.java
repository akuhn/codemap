package example;

import static ch.akuhn.util.Get.take;
import static ch.akuhn.util.Out.puts;

import java.io.File;
import java.io.FileNotFoundException;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {

        Corpus corpus = new Corpus();
        corpus.scanFolder(new File("../Fame"), ".java");
        corpus.scanFolder(new File("."), ".java");

        puts(corpus);

        puts(corpus.terms().toLowerCase().sortedCounts());

        TermDocumentMatrix tdm = new TermDocumentMatrix();

        tdm.addCorpus(corpus);

        puts(tdm);

        puts(tdm.density());

        tdm = tdm.rejectAndWeight();

        puts(tdm);

        puts(tdm.density());

        puts(tdm.terms().sortedCounts());

        LatentSemanticIndex lsi = tdm.createIndex();

        puts(take(10, lsi.rankDocumentsByTerm("bag")));
        puts(take(10, lsi.rankTermsByTerm("bag")));
        puts(take(10, lsi.rankDocumentsByTerm("famix")));
        puts(take(10, lsi.rankDocumentsByQuery("split string by lower- and upper-case")));

    }

}
