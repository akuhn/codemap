package meander.jsme2009.eclipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import meander.jsme2009.Serializer;
import meander.jsme2009.Serializer.MSEDocument;
import meander.jsme2009.Serializer.MSERelease;
import meander.jsme2009.junit.ComputeHausdorff;

import ch.akuhn.fame.Repository;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Throw;
import ch.deif.meander.MDS;


public class EclipseAllAtOnce {
    
    static final String[] VERSIONS = {
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
    };

    
    public TermDocumentMatrix importTDM() {
        try {
            return TermDocumentMatrix.readFrom(new Scanner(new File(EclipseCorpus.TDMFILE_10)));
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public Repository locationsRepository() {
        TermDocumentMatrix TDM = this.importTDM();
        System.out.println(TDM);
        TDM = TDM.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = TDM.createIndex();
        System.out.println("Computing MDS...");
        MDS mds = MDS.fromCorrelationMatrix(i);
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project("Eclipse");
        for (String version: VERSIONS) {
            ser.release(version);
            for (Document each: i.documents) {
                if (!each.version().equals(version)) continue;
                int index = i.documents.get(each);
                ser.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize()), each.name());
            }
        }
        return ser.model();
    }
    
    public void run() {
//        Repository model = locationsRepository();
//        System.out.printf("# num(doc) = %d; num(rel) = %d\n", 
//                model.count(MSEDocument.class),
//                model.count(MSERelease.class));
//        model.exportMSEFile("mse/eclipse_meander.mse");
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/eclipse_meander.mse");
        new ComputeHausdorff(ser).run();     
    }
 
    public static void main(String[] args) {
        // Run with -Xmx256M for greater justice
        new EclipseAllAtOnce().run();
    }
    
}
