package meander.jsme2009.struts;

import java.util.Arrays;
import java.util.List;

import ch.akuhn.fame.Repository;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.VersionNumber;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.CacheMap;
import ch.deif.meander.MDS;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;


public class ComputeLSIAndMDS {
    
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

    public static final String FILENAME = 
            "mse/struts_corpus.mse";
    
    public Corpus corpus() {
        Serializer ser = new Serializer();
        ser.model().importMSEFile(FILENAME);
        System.out.printf("# num(doc) = %d; num(rel) = %d\n", 
                ser.model().count(MSEDocument.class),
                ser.model().count(MSERelease.class));
        MSEProject project = ser.model().all(MSEProject.class).iterator().next();
        Corpus corpus = new Corpus();
        for (MSERelease version: project.releases) {
            if (!VERSIONS.contains(version.name)) continue;
            for (MSEDocument each: version.documents) {
                assert each.name != null;
                corpus.add(new Document(each.name, each.terms, versionNumbers.get(version.name)));
            }
        }
        return corpus;
    }
    
    // Magic map that creates and caches instances of VersionNumber using String a parameters.
    private CacheMap<String,VersionNumber> versionNumbers = CacheMap.instances(VersionNumber.class);
    
    public Repository locationsRepository() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.addCorpus(corpus());
        tdm = tdm.rejectAndWeight();
        System.out.println("Computing LSI...");
        LatentSemanticIndex i = tdm.createIndex();
        tdm = null;
        for (Document each: i.documents) each.terms = null;
        System.gc();
        System.out.println("Computing MDS...");
        MDS mds = MDS.fromCorrelationMatrix(i.documentCorrelation());
        System.out.println("Done.");
        Serializer ser = new Serializer();
        ser.project("JUnit");
        String version = "";
        int index = 0;
        for (Document each: i.documents) {
            if (!each.version.string.equals(version)) {
                version = each.version.string;
                ser.release(version);
            }
            ser.location(mds.x[index], mds.y[index], Math.sqrt(each.termSize), each.handle);
            index++;
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
        // Run with -Xmx400M for greater justice
        new ComputeLSIAndMDS().run();
    }
    
}
