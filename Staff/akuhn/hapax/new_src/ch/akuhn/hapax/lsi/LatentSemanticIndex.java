package ch.akuhn.hapax.lsi;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Index;
import ch.akuhn.hapax.linalg.SVD;

public class LatentSemanticIndex extends SVD {

    private Index<Document> documents; 
    private Index<CharSequence> terms;     
    
    
}
