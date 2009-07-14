package meander.jsme2009.eclipse;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.SizeOf;
import ch.akuhn.util.query.GroupedBy;

public class EclipseCorpus {

    public static final String DATADIR = "data/eclipse";
    public static final String TDMFILE = "mse/eclipse.TDM";
    public static final int DEZIMATION_FACTOR = 10;
    public static final String TDMFILE_10 = "mse/eclipse_"+DEZIMATION_FACTOR+".TDM";

    public static void main(String... args) throws Throwable {
        
        // XXX must run with -Xmx800M for greater justice.
        
        // extractCorpus();
        dezimateCorpus();
        
    }

    public static void dezimateCorpus() throws FileNotFoundException {
        TermDocumentMatrix TDM = TermDocumentMatrix.readFrom(new Scanner(new File(TDMFILE)));
        GroupedBy<Document> groupedByName = GroupedBy.from(TDM.documents());
        for (GroupedBy<Document> each: groupedByName) {
            each.yield = each.element.name();
        }
        TermDocumentMatrix TDM_2 = new TermDocumentMatrix();
        int tally = 0;
        for (Collection<Document> each: groupedByName.result().values()) {
            if ((tally++ % DEZIMATION_FACTOR) != 0) continue;
            for (Document doc: each) {
                TDM_2.makeDocument(doc.name(), doc.version()).addTerms(doc.terms());
            }
        }
        System.out.println(TDM_2);
        TDM_2.storeOn(TDMFILE_10);
        
    }

    public static void extractCorpus() {
        TermDocumentMatrix TDM = new TermDocumentMatrix();
        
        new Importer(TDM)
            .importAllZipArchivesPackageWise(new File(DATADIR), ".java");
        System.out.println(TDM);
        System.out.println(SizeOf.deepSizeOf(TDM));
        TDM = TDM.toLowerCase().rejectHapaxes().rejectStopwords();
        System.out.println(TDM);
        System.out.println(SizeOf.deepSizeOf(TDM));
        
        TDM.storeOn(TDMFILE);
    }
    
}
