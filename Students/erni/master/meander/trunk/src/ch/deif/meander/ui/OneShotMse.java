package ch.deif.meander.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ch.akuhn.hapax.index.TermDocumentMatrix;

public class OneShotMse {
    
    private static final String FILENAME = "mse/junit.TDM";
    private static TermDocumentMatrix tdm;    
    
    public static void main(String ... args) throws FileNotFoundException {
        tdm = TermDocumentMatrix.readFrom(new Scanner(new File(FILENAME)));     
    }

}
