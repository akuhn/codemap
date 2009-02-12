package meander.jsme2009.junit;

import ch.akuhn.fame.Tower;
import ch.akuhn.fame.parser.InputSource;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.VersionNumber;
import ch.akuhn.util.Get;
import ch.deif.meander.Serializer;
import ch.unibe.jsme2009.HapaxDoc;

/**
 * Migrates MSE, from Software Cartographer (Smalltalk) to Meander (Java).
 * 
 * @author Adrian Kuhn
 *
 */
public class OneshotMigration {

    public static void main(String[] args) {
        
        assert false : "Don't run me unless you really want.";
        
        Tower t = new Tower();
        t.metamodel.with(HapaxDoc.class);
        t.model.importMSE(InputSource.fromFilename("mse/junit_smalltalk.h.mse"));
        Corpus corpus = new Corpus();
        for (HapaxDoc each: Get.sorted(t.model.all(HapaxDoc.class))) {
            corpus.add(new Document(each, each.terms));
        }
        Serializer ser = new Serializer();
        ser.project("JUnit");
        VersionNumber version = null;
        for (Document each: corpus.documents()) {
            HapaxDoc doc = ((HapaxDoc) each.handle);
            if (!doc.version.equals(version)) {
                ser.release(doc.version.string);
                version = (doc.version);
            }
            ser.document(doc.name, doc.terms);
        }
        ser.model().exportMSEFile("mse/junit_corpus.mse");
        
    }
    
}
