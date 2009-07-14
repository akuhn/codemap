package meander.jsme2009.junit;

import meander.jsme2009.Serializer;
import meander.jsme2009.Serializer.MSEDocument;
import meander.jsme2009.Serializer.MSEProject;
import meander.jsme2009.Serializer.MSERelease;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class OneShot {

    public static void main(String... args) {
        
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/junit.mse");
        MSEProject project = ser.model().all(MSEProject.class).iterator().next();
        TermDocumentMatrix corpus = new TermDocumentMatrix();
        for (MSERelease version: project.releases) {
            for (MSEDocument each: version.documents) {
                assert each.name != null;
                corpus.makeDocument(each.name, version.name).addTerms(new Terms(each.terms));
            }
        }
        corpus.storeOn("mse/junit_new.TDM");

        
    }
    
}
