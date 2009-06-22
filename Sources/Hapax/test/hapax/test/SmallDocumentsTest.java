package hapax.test;

import org.junit.Test;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;

public class SmallDocumentsTest {

    @Test
    public void corpusWithSmallDocuments() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        tdm.makeDocument("m1").addTerms(new Terms("Lorem ipsum"));
        tdm.makeDocument("m2").addTerms(new Terms("Lorem dolor"));
        tdm.makeDocument("m3").addTerms(new Terms("ipsum dolor"));
        LatentSemanticIndex lsi = tdm.rejectAndWeight().createIndex();
        System.out.println(lsi);
    }
    
}
