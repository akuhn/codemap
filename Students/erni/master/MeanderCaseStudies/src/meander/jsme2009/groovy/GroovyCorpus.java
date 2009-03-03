package meander.jsme2009.groovy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipException;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Importer;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.SizeOf;
import ch.akuhn.util.query.GroupedBy;


public class GroovyCorpus {

    public static final String TDMFILE = "mse/groovy.TDM";
    public static final String DATA = "data/groovy";
    public static final int DEZIMATION_FACTOR = 5;
    public static final String TDMFILE_10 = "mse/groovy_"+DEZIMATION_FACTOR+".TDM";

    public static void main(String[] args) throws ZipException, IOException {
        
        // XXX Must run with -Xmx128M for greater justice
        
        TermDocumentMatrix TDM = (TermDocumentMatrix) new Importer(new TermDocumentMatrix())
                .importAllZipArchives(new File(DATA), ".java");
        SizeOf.sizeOf(TDM);
        TDM = TDM.toLowerCase().rejectHapaxes().rejectStopwords();
        System.out.println(TDM);
        SizeOf.sizeOf(TDM);
        
        TDM.storeOn(TDMFILE);
        
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

}
