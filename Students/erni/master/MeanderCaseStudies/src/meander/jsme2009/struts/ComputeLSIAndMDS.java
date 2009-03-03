package meander.jsme2009.struts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import ch.akuhn.fame.Repository;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.akuhn.util.Throw;
import ch.akuhn.util.query.GroupedBy;
import ch.deif.meander.MDS;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSERelease;


public class ComputeLSIAndMDS {
    
    private static final int DEZIMATION_FACTOR = 10;

    public static final List<String> VERSIONS = Arrays.asList(new String[] {
        "jakarta-struts-1.0.2-src.zip",
        "jakarta-struts-1.1-b1-src.zip",
        "jakarta-struts-1.1-b2-src.zip", 
        "jakarta-struts-1.1-b3-src.zip",
        "jakarta-struts-1.1-rc1-src.zip",
        "jakarta-struts-1.1-rc2-src.zip",
        "jakarta-struts-1.1-src.zip",
        "jakarta-struts-1.2.2-src.zip",
        "jakarta-struts-1.2.4-src.zip",
        "struts-1.2.6-src.zip",
        "struts-1.2.7-src.zip",
        "struts-1.2.8-src.zip",
        "struts-1.2.9-src.zip",
        "struts-1.3.5-src.zip",
        "struts-2.0.1-src.zip",
        "struts-2.0.5-src.zip",
        "struts-2.0.6-src.zip",
//        "struts-1.3.8-src.zip",
//        "struts-1.3.9-src.zip",
        "struts-2.0.8-src.zip",
        "struts-2.0.9-src.zip",
        "struts-2.0.11-src.zip",
        "struts-2.0.11.1-src.zip",
        "struts-2.1.2-src.zip",
//        "struts-2.0.12-src.zip",
//        "struts-2.0.14-src.zip",
//        "struts-1.3.10-src.zip",
        "struts-2.1.6-src.zip",
    });

    
    public static TermDocumentMatrix importTDM() {
        try {
            TermDocumentMatrix TDM;
            TDM = TermDocumentMatrix.readFrom(new Scanner(new File("mse/struts.TDM")));
            System.out.println(TDM);
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
            TDM_2.storeOn(StrutsIncrementalCaseStudy.TDMFILE);
            return TDM_2;
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public Repository locationsRepository() {
        TermDocumentMatrix TDM = importTDM();
        TDM = TDM.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = TDM.createIndex();
        System.out.println("Computing MDS...");
        MDS mds = MDS.fromCorrelationMatrix(i);
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project("JUnit");
        String version = "";
        for (Document each: Get.sorted(i.documents)) {
            if (!each.version().equals(version)) {
                version = each.version();
                ser.release(version);
            }
            int index = i.documents.get(each);
            ser.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each.name());
        }
        return ser.model();
    }
    
    public void run() {
        Repository model = locationsRepository();
        System.out.printf("# num(doc) = %d; num(rel) = %d\n", 
                model.count(MSEDocument.class),
                model.count(MSERelease.class));
        model.exportMSEFile("mse/struts_meander.mse");
     }
 
    public static void main(String[] args) {
        // Run with -Xmx256M for greater justice
        new ComputeLSIAndMDS().run();
    }
    
}
