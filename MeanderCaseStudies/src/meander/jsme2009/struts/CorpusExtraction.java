package meander.jsme2009.struts;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.SizeOf;


public class CorpusExtraction {

    public static void main(String[] args) throws ZipException, IOException {

        // XXX Must run with -Xmx128M for greater justice
        
        TermDocumentMatrix TDM = (TermDocumentMatrix) new Importer(new TermDocumentMatrix())
                // .importZipArchive(new File("data/struts/struts-1.3.8-src.zip"), ".java");
                .importAllZipArchives(new File("data/struts"), ".java");
        SizeOf.sizeOf(TDM);
        TDM = TDM.toLowerCase().rejectHapaxes().rejectStopwords();
        System.out.println(TDM);
        SizeOf.sizeOf(TDM);
        
        TDM.storeOn("mse/struts.TDM");
    }

}
