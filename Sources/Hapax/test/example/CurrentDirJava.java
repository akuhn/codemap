package example;

import static ch.akuhn.util.Get.first;
import static ch.akuhn.util.Out.puts;

import java.io.File;
import java.io.FileNotFoundException;

import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.corpus.TermBagCorpus;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.hapax.util.Serialization;

public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {

        TermBagCorpus corpus = new TermBagCorpus();
        Importer importer = new Importer(corpus);
        importer.importAllFiles(new File("../Fame"), ".java");
        importer.importAllFiles(new File("."), ".java");

        puts(corpus);

        puts(corpus.terms().toLowerCase().sortedCounts());

        TermDocumentMatrix tdm = new TermDocumentMatrix();

        tdm.addCorpus(corpus);

        puts(tdm);

        puts(tdm.density());

        tdm = tdm.rejectAndWeight();

        puts(tdm);

        puts(tdm.density());

        puts(tdm.termBag().sortedCounts());

        LatentSemanticIndex lsi = tdm.createIndex();

        puts(first(10, lsi.rankDocumentsByTerm("bag")));
        puts(first(10, lsi.rankTermsByTerm("bag")));
        puts(first(10, lsi.rankDocumentsByTerm("famix")));
        puts(first(10, lsi.rankDocumentsByQuery("split string by lower- and upper-case")));

        Serialization mse;
        
        mse = new Serialization();
        mse.add(lsi);
        String string = mse.t.model.exportMSE();
        
        System.out.println(string);
        
        mse = new Serialization();
        mse.t.model.importMSE(string);
        System.out.println(mse.t.model.getElements());
        
    }

}
