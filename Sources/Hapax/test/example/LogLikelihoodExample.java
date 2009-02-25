package example;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.corpus.SimpleCorpus;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LogLikelihood;
import ch.akuhn.util.Out;

public class LogLikelihoodExample {

    public static void main(String... args) {
        
        Corpus c1 = new Importer(new SimpleCorpus()).importAllFiles(new File("../Fame"), ".java");
        Corpus c2 = new Importer(new SimpleCorpus()).importAllFiles(new File("../CELLS"), ".java");
        
        System.out.println(c1);
        System.out.println(c2);
        
        SortedSet<LogLikelihood> list = new TreeSet<LogLikelihood>();
        for (String each: (union(c1.terms(), c2.terms())).elements()) {
            list.add(new LogLikelihood(c1.terms(), c2.terms(), each));
        }
        
        Out.puts(list);
        
    }

    private static Terms union(Terms t1, Terms t2) {
        Terms terms = new Terms();
        terms.addAll(t1);
        terms.addAll(t2);
        return terms;
    }
    
}
