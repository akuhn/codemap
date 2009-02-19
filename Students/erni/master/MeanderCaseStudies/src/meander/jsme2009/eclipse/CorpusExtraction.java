package meander.jsme2009.eclipse;


import java.io.File;

import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.SizeOf;

public class CorpusExtraction {

    public static void main(String... args) {
        
        // XXX must run with -Xmx800M for greater justice.
        
        TermDocumentMatrix TDM = new TermDocumentMatrix();
        
        new Importer(TDM)
            // .importZipArchivePackageWise("data/eclipse/eclipse-sourceBuild-srcIncluded-2.0.1.zip", ".java");
            .importAllZipArchivesPackageWise(new File("data/eclipse"), ".java");
        
        System.out.println(TDM);
        System.out.println(SizeOf.deepSizeOf(TDM));
        TDM = TDM.toLowerCase().rejectHapaxes().rejectStopwords();
        System.out.println(TDM);
        System.out.println(SizeOf.deepSizeOf(TDM));
        
        TDM.storeOn("mse/eclipse.TDM");
        
    }
    
}
