package ch.akuhn.hapax.index;

import static ch.akuhn.foreach.For.matrix;
import static ch.akuhn.foreach.For.range;
import static ch.akuhn.foreach.For.withIndex;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import ch.akuhn.foreach.Each;
import ch.akuhn.foreach.EachXY;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.linalg.Matrix;
import ch.akuhn.hapax.linalg.SVD;
import ch.akuhn.hapax.linalg.SymetricMatrix;
import ch.akuhn.util.Providable;
import ch.akuhn.util.Bag.Count;

public class LatentSemanticIndex implements Serializable {

    private static final long serialVersionUID = 1337L;

    private static final int VERSION_1 = 0x20080830;

    private AssociativeList<String> documents;
    private AssociativeList<String> terms;
    private SVD svd; 

    private double[] globalWeighting;

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws Exception {
        int version = in.readInt();
        if (version != VERSION_1) throw new Error();
        terms = new AssociativeList<String>((Iterable<String>) in.readObject());
        documents = new AssociativeList<String>((Iterable<String>) in.readObject());
        svd = (SVD) in.readObject();
        if (!this.invariant()) throw new Error();
    }

    private boolean invariant() {
        return true; // TODO Auto-generated method stub
    }

    private void writeObject(ObjectOutputStream out) throws Exception {
        out.writeInt(VERSION_1);
        out.writeObject(terms.asList());
        out.writeObject(documents.asList());
        out.writeObject(svd);
    }

    public LatentSemanticIndex(
            AssociativeList<String> terms, 
            AssociativeList<String> documents,
            double[] globalWeighting, 
            SVD svd) {
        this.documents = documents;
        this.terms = terms;
        this.svd = svd;
        this.globalWeighting = globalWeighting;
        assert globalWeighting.length == terms.size();
        if (svd.getRank() == 0) return;
        if (svd.rowCount() != terms.size()) this.svd = svd.transposed();
        assert svd.rowCount() == terms.size();
        assert svd.columnCount() == documents.size();
    }

    public double[] createPseudoDocument(String string) {
        // apply: CamelCaseScanner, PorterStemmer, toLowerCase, and weighting
        Terms query = new Terms(string).toLowerCase().stem();
        return createPseudoDocument(query);
    }

    public double[] createPseudoDocument(Terms query) {
        double[] weightings = new double[termCount()];
        // iterate over query, assume: quert.size() <<< terms.size()
        for (Count<String> each: query.counts()) {
            int index = terms.get(each.element);
            if (index < 0) continue;
            double weight = (globalWeighting == null ? 1 : globalWeighting[index]);
            weightings[index] = each.count * weight;
        }
        return svd.makePseudoV(weightings);
    }

    public Ranking<String> rankDocumentsByDocument(String d) {
        Ranking<String> ranking = new Ranking<String>();
        int n = documents.get(d);
        for (Each<String> each: withIndex(documents)) {
            ranking.add(each.value, svd.similarityVV(n, each.index));
        }
        return ranking.sort();
    }

    public Ranking<String> rankDocumentsByQuery(String query) {
        Ranking<String> ranking = new Ranking<String>();
        double[] pseudo = createPseudoDocument(query);
        for (Each<String> each: withIndex(documents)) {
            ranking.add(each.value, svd.similarityV(each.index, pseudo));
        }
        return ranking.sort();
    }

    public Ranking<String> rankDocumentsByQuery(Terms query) {
        Ranking<String> ranking = new Ranking<String>();
        double[] pseudo = createPseudoDocument(query);
        for (Each<String> each: withIndex(documents)) {
            ranking.add(each.value, svd.similarityV(each.index, pseudo));
        }
        return ranking.sort();
    }


    public Ranking<String> rankDocumentsByTerm(String term) {
        Ranking<String> ranking = new Ranking<String>();
        int n = terms.get(term);
        assert n >= 0;
        for (Each<String> each: withIndex(documents)) {
            ranking.add(each.value, svd.similarityUV(n, each.index));
        }
        return ranking.sort();
    }

    public Ranking<CharSequence> rankTermsByDocument(String d) {
        Ranking<CharSequence> ranking = new Ranking<CharSequence>();
        int n = documents.get(d);
        for (Each<String> each: withIndex(terms)) {
            ranking.add(each.value, svd.similarityUV(each.index, n));
        }
        return ranking.sort();
    }

    public Ranking<CharSequence> rankTermsByTerm(String term) {
        Ranking<CharSequence> ranking = new Ranking<CharSequence>();
        int n = terms.get(term);
        for (Each<String> each: withIndex(terms)) {
            ranking.add(each.value, svd.similarityUU(n, each.index));
        }
        return ranking.sort();
    }

    public Matrix documentCorrelation() {
        Matrix correlation = new SymetricMatrix(documents.size());
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
                iter = matrix(documents.size(), documents.size()).iterator();
            }
            @Override
            public Double provide() {
                if (!iter.hasNext()) return done();
                EachXY each = iter.next();
                return svd.similarityVV(each.x, each.y);
            }      
        };
    }

    //    public LatentSemanticIndex select(String version) {
    //    	AssociativeList<String> selection = new AssociativeList<Document>();
    //        for (String each: documents) {
    //            if (each.version().equals(version)) selection.add(each);
    //        }
    //        int[] indices = new int[selection.size()];
    //        for (int n = 0; n < indices.length; n++) {
    //            indices[documents.get(selection.get(n))] = n;
    //        }
    //        return new LatentSemanticIndex(
    //                terms, selection,
    //                new double[selection.size()], // TODO copy global weighting
    //                svd.withSelectV(indices));
    //    }

    public int documentCount() {
        return documents.size();
    }

    public int termCount() {
        return terms.size();
    }

    public void updateDocument(String doc, Terms contents) {
        double[] newDocument = createPseudoDocument(contents);
        int index = documents.get(doc);
        if (index >= 0) {
            svd = svd.withReplaceV(index, newDocument);
        }
        else {
            documents.add(doc);
            svd = svd.withAppendV(newDocument);
        }
    }

    public void removeDocument(String doc) {
        int index = documents.remove(doc);
        if (index < 0) return;
        svd = svd.withoutV(index);
        // todo adapate termCounts
    }

    public Iterable<String> documents() {
        return documents;
    }

    public int getTermCount(String document) {
        int index = documents.get(document);
        if (index < 0) return 0;
        return (int) (svd.lengthV(index) * 1000);
    }

}
