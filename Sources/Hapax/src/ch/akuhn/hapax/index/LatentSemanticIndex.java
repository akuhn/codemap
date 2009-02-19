package ch.akuhn.hapax.index;

import static ch.akuhn.util.Each.withIndex;
import static ch.akuhn.util.Interval.range;

import java.util.Iterator;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.linalg.SVD;
import ch.akuhn.hapax.linalg.SymetricMatrix;
import ch.akuhn.util.Each;
import ch.akuhn.util.EachXY;
import ch.akuhn.util.Providable;
import ch.akuhn.util.Bag.Count;

public class LatentSemanticIndex {

    public final Index<Document> documents;
    public final double[] globalWeighting;
    public final SVD svd; 
    public final Index<String> terms;

    public LatentSemanticIndex(Index<String> terms, Index<Document> documents,
            double[] globalWeighting, SVD svd) {
        this.documents = documents;
        this.terms = terms;
        if (svd.Ut[0].length != terms.size()) svd = svd.transposed();
        this.svd = svd;
        assert svd.Ut[0].length == terms.size();
        assert svd.Vt[0].length == documents.size();
        this.globalWeighting = globalWeighting;
    }

    public double[] createPseudoDocument(String string) {
        // apply: CamelCaseScanner, PorterStemmer, toLowerCase, and weighting
        Terms query = new Terms(string).toLowerCase().stem();
        double[] pseudo = new double[svd.s.length];
        for (Count<String> each: query.counts()) {
            int t0 = terms.get(each.element);
            if (t0 < 0) continue;
            double weight = each.count * (globalWeighting == null ? 1 : globalWeighting[t0]);
            for (int n: range(svd.s.length)) {
                pseudo[n] += weight * svd.Ut[n][t0];
            }
        }
        for (int n: range(svd.s.length)) {
            pseudo[n] /= svd.s[n];
        }
        return pseudo;
    }

    public Ranking<Document> rankDocumentsByDocument(Document d) {
        Ranking<Document> ranking = new Ranking<Document>();
        int n = documents.get(d);
        for (Each<Document> each: withIndex(documents)) {
            ranking.add(each.element, svd.similarityVV(n, each.index));
        }
        return ranking.sort();
    }

    public Ranking<Document> rankDocumentsByQuery(String query) {
        Ranking<Document> ranking = new Ranking<Document>();
        double[] pseudo = createPseudoDocument(query);
        for (Each<Document> each: withIndex(documents)) {
            ranking.add(each.element, svd.similarityV(each.index, pseudo));
        }
        return ranking.sort();
    }

    public Ranking<Document> rankDocumentsByTerm(String term) {
        Ranking<Document> ranking = new Ranking<Document>();
        int n = terms.get(term);
        for (Each<Document> each: withIndex(documents)) {
            ranking.add(each.element, svd.similarityUV(n, each.index));
        }
        return ranking.sort();
    }

    public Ranking<CharSequence> rankTermsByDocument(Document d) {
        Ranking<CharSequence> ranking = new Ranking<CharSequence>();
        int n = documents.get(d);
        for (Each<String> each: withIndex(terms)) {
            ranking.add(each.element, svd.similarityUV(each.index, n));
        }
        return ranking.sort();
    }

    public Ranking<CharSequence> rankTermsByTerm(String term) {
        Ranking<CharSequence> ranking = new Ranking<CharSequence>();
        int n = terms.get(term);
        for (Each<String> each: withIndex(terms)) {
            ranking.add(each.element, svd.similarityUU(n, each.index));
        }
        return ranking.sort();
    }

    public SymetricMatrix documentCorrelation() {
        SymetricMatrix correlation = new SymetricMatrix(documents.size());
        for (int row: range(documents.size())) {
            for (int column: range(documents.size())) {
                correlation.put(row, column, svd.similarityVV(row, column));
            }
        }
        return correlation;
    }
    
    public Iterable<Double> documentCorrelations() {
        return new Providable<Double>() {
            private Iterator<EachXY> iter;
            @Override
            public void initialize() {
                iter = new EachXY(documents.size(), documents.size()).iterator();
            }
            @Override
            public Double provide() {
                if (!iter.hasNext()) return done();
                EachXY each = iter.next();
                return svd.similarityVV(each.x, each.y);
            }      
        };
    }
    
}
