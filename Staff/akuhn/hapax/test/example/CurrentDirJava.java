package example;

import java.io.File;

import ch.akuhn.hapax.Corpus;
import ch.akuhn.util.Extensions;


public class CurrentDirJava {

    public static void main(String[] args) {
        
        Corpus corpus = new Corpus();
        corpus.scanFolder(new File("."), ".java");
        
        System.out.println(corpus);
        
        Extensions.puts( corpus.allTerms().toLowerCase().sortedCounts() );
        
    }
    
}
