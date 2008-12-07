package example;

import static ch.akuhn.util.Extensions.puts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import ch.akuhn.hapax.Corpus;
import ch.akuhn.hapax.SparseVector;
import ch.akuhn.hapax.TermDocumentMatrix;


public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {
        
        Corpus corpus = new Corpus();
        corpus.scanFolder(new File(".."), ".java");
        
        puts( corpus );
        
        puts( corpus.terms().toLowerCase().sortedCounts() );
        
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        
        tdm.addCorpus(corpus);
        
        puts( tdm );
        
        //puts( SparseVector.tally$grow, SparseVector.tally$insert, SparseVector.tally$total );
        
        puts( tdm.density() );
        
        tdm.storeSparseOn(System.out);
        
        tdm.storeSparseOn(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("matrix.txt")))));
        
    }
    
}
