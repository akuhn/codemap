package example;

import static ch.akuhn.util.Extensions.puts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.linalg.SVD;
import ch.akuhn.hapax.linalg.SparseVector;
import ch.akuhn.hapax.lsi.GlobalWeighting;
import ch.akuhn.hapax.lsi.LocalWeighting;
import ch.akuhn.hapax.lsi.TermDocumentMatrix;


public class CurrentDirJava {

    public static void main(String[] args) throws FileNotFoundException {
        
        Corpus corpus = new Corpus();
        corpus.scanFolder(new File(".."), ".java");
        
        puts( corpus );
        
        puts( corpus.terms().toLowerCase().sortedCounts() );
        
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        
        tdm.addCorpus(corpus);
        
        puts( tdm );
        
        puts( tdm.density() );
        
        tdm = tdm.rejectAndWeight();
        
        puts( tdm );

        puts( tdm.density() );

        SVD svd = SVD.fromMatrix(tdm, 20);
        

        puts( svd.time );
        puts( svd.Ut[0].length );
        puts( svd.Vt[0].length );
        
    }
    
}
