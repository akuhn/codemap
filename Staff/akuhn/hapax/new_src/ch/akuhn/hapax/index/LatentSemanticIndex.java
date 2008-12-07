package ch.akuhn.hapax.index;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Index;
import ch.akuhn.hapax.linalg.SVD;
import ch.akuhn.util.query.Each;

public class LatentSemanticIndex {

    private Index<Document> documents; 
    private Index<CharSequence> terms;     
    private SVD svd;
    
    public LatentSemanticIndex(Index<Document> documents,
            Index<CharSequence> terms, SVD svd) {
        this.documents = documents;
        this.terms = terms;
        this.svd = svd;
        if (svd.Ut[0].length != terms.size()) {
            float[][] temp = svd.Ut; svd.Ut = svd.Vt; svd.Vt = temp;
        }
        assert svd.Ut[0].length == terms.size();
        assert svd.Vt[0].length == terms.size();
    }

    public Ranking<Document> rankDocumentsByTerm(CharSequence term) {
        Ranking<Document> ranking = new Ranking<Document>();
        int n = terms.get(term);
        for (Each<Document> each: Each.withIndex(documents)) {
            ranking.add(each.element, svd.similarityUV(n, each.index));
        }
        return ranking.sort();
    }
    
    public Ranking<CharSequence> rankTermsByTerm(CharSequence term) {
        Ranking<CharSequence> ranking = new Ranking<CharSequence>();
        int n = terms.get(term);
        for (Each<CharSequence> each: Each.withIndex(terms)) {
            ranking.add(each.element, svd.similarityUU(n, each.index));
        }
        return ranking.sort();
    }
    
    
}
