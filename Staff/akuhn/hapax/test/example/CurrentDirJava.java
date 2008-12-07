package example;

import static ch.akuhn.util.Extensions.puts;

import java.io.File;
import java.io.FileNotFoundException;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {

        Corpus corpus = new Corpus();
        corpus.scanFolder(new File(".."), ".java");

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

        puts(lsi.rankDocumentsByTerm("bag"));
        puts(lsi.rankTermsByTerm("bag"));
        puts(lsi.rankDocumentsByTerm("famix"));
        puts(lsi.rankDocumentsByQuery("split string by lower- and upper-case"));

    }

}
