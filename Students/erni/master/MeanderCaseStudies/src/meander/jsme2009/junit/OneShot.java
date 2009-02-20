package meander.jsme2009.junit;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.SimpleCorpus;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

public class OneShot {

    public static void main(String... args) {
        
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/junit_corpus.mse");
        MSEProject project = ser.model().all(MSEProject.class).iterator().next();
        TermDocumentMatrix corpus = new TermDocumentMatrix();
        for (MSERelease version: project.releases) {
            for (MSEDocument each: version.documents) {
                assert each.name != null;
                corpus.makeDocument(each.name, version.name).addTerms(new Terms(each.terms));
            }
        }
        corpus.storeOn("mse/junit.TDM");

        
    }
    
}
