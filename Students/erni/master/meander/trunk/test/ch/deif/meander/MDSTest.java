package ch.deif.meander;

import java.io.File;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.SimpleCorpus;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

public class MDSTest {
    
    private static final String FILENAME = "mse/junit_corpus.mse";
    private static final String DEFAULT_VERSION = "junit4.0.zip";
    
    /**
     * Load a corpus containing all documents of the given version.
     * @param versionName
     * @return
     */
    public Corpus corpus(String versionName) {
        assert new File(FILENAME).isFile();
        Serializer ser = new Serializer();
        ser.model().importMSEFile(FILENAME);
        MSEProject project = ser.model().all(MSEProject.class).iterator().next();
        Corpus corpus = new SimpleCorpus();
        for (MSERelease version: project.releases) {
            if (!versionName.equals(version.name)) continue;
            for (MSEDocument each: version.documents) {
                assert each.name != null;
                corpus.makeDocument(each.name, version.name).addTerms(new Terms(each.terms));
            }
        }
        return corpus;
    }
    
    public static void main(String... args) {
        
        // TODO it seems, class MultiDimensionalScaling is missing or not commit.
        
//        Corpus corpus = new MDSTest().corpus(DEFAULT_VERSION);
//        TermDocumentMatrix tdm = new TermDocumentMatrix();
//        tdm.addCorpus(corpus);
//        tdm.rejectAndWeight();
//        LatentSemanticIndex lsi = tdm.createIndex();
//        MultiDimensionalScaling.fromCorrelationMatrix(lsi);
    }

}
