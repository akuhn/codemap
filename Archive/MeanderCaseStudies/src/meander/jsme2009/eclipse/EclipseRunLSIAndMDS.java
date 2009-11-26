package meander.jsme2009.eclipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import meander.jsme2009.Serializer;
import meander.jsme2009.Serializer.MSEDocument;
import meander.jsme2009.Serializer.MSERelease;

import ch.akuhn.fame.Repository;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.akuhn.util.Throw;
import ch.akuhn.util.query.GroupedBy;
import ch.deif.meander.MDS;


public class EclipseRunLSIAndMDS {
    
    public static final String PROJNAME = "Eclipse";

    public static final String MSE_FILE = "mse/eclipse_meander.mse";

    public static final String TDM_FILE = "mse/eclipse.TDM";

    public static final int DEZIMATION_FACTOR = 20;

    public static final List<String> VERSIONS = Arrays.asList(new String[] {
            "eclipse-sourceBuild-srcIncluded-2.0.zip",
            "eclipse-sourceBuild-srcIncluded-2.0.1.zip",
            "eclipse-sourceBuild-srcIncluded-2.0.2.zip",
            "eclipse-sourceBuild-srcIncluded-2.1.zip",
            "eclipse-sourceBuild-srcIncluded-2.1.1.zip",
            "eclipse-sourceBuild-srcIncluded-2.1.2.zip",
            "eclipse-sourceBuild-srcIncluded-2.1.3.zip",
            "eclipse-sourceBuild-srcIncluded-3.0.zip",
            "eclipse-sourceBuild-srcIncluded-3.0.1.zip",
            "eclipse-sourceBuild-srcIncluded-3.0.2.zip",
            "eclipse-sourceBuild-srcIncluded-3.1.zip",
            "eclipse-sourceBuild-srcIncluded-3.1.1.zip",
            "eclipse-sourceBuild-srcIncluded-3.1.2.zip",
            "eclipse-sourceBuild-srcIncluded-3.2.zip",
            "eclipse-sourceBuild-srcIncluded-3.2.1.zip",
            "eclipse-sourceBuild-srcIncluded-3.2.2.zip",
            "eclipse-sourceBuild-srcIncluded-3.3.zip",
            "eclipse-sourceBuild-srcIncluded-3.3.1.zip",
            "eclipse-sourceBuild-srcIncluded-3.3.1.1.zip",
            "eclipse-sourceBuild-srcIncluded-3.3.2.zip",

    });

    
    public TermDocumentMatrix importTDM() {
        try {
            TermDocumentMatrix TDM;
            TDM = TermDocumentMatrix.readFrom(new Scanner(new File(TDM_FILE)));
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
            return TDM_2;
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public Repository locationsRepository() {
        TermDocumentMatrix TDM = this.importTDM();
        TDM = TDM.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = TDM.createIndex();
        System.out.println("Computing MDS...");
        MDS mds = MDS.fromCorrelationMatrix(i);
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project(PROJNAME);
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
        model.exportMSEFile(MSE_FILE);
     }
 
    public static void main(String[] args) {
        // Run with -Xmx256M for greater justice
        new EclipseRunLSIAndMDS().run();
    }
    
}
