package ch.akuhn.hapax.index;

import static ch.akuhn.util.Each.withIndex;
import static ch.akuhn.util.Pair.zip;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Index;
import ch.akuhn.hapax.corpus.PorterStemmer;
import ch.akuhn.hapax.corpus.Stemmer;
import ch.akuhn.hapax.corpus.Stopwords;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.linalg.SVD;
import ch.akuhn.hapax.linalg.SparseMatrix;
import ch.akuhn.hapax.linalg.Vector;
import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.Each;
import ch.akuhn.util.Pair;
import ch.akuhn.util.Bag.Count;

public class TermDocumentMatrix extends SparseMatrix {

    private Index<Document> documents; // columns
    private double[] globalWeighting;
    private Index<String> terms; // rows

    public TermDocumentMatrix() {
        super(0, 0);
        this.terms = new Index<String>();
        this.documents = new Index<Document>();
    }

    public TermDocumentMatrix(Index<String> terms, Index<Document> documents) {
        super(terms.size(), documents.size());
        this.terms = terms.clone();
        this.documents = documents.clone();
    }

    public void addCorpus(Corpus corpus) {
        for (Document document: corpus.documents()) {
            int column = addDocument(document);
            Terms terms = corpus.get(document);
            for (Count<String> each: terms.counts()) {
                int row = addTerm(each.element);
                add(row, column, each.count);
            }
        }
    }

    public int addDocument(Document document) {
        int index = documents.add(document);
        if (index == columnSize()) addColumn();
        return index;
    }

    public int addTerm(String term) {
        int index = terms.add(term);
        if (index == rowSize()) addRow();
        return index;
    }

    private void addTerm(String term, Vector values) {
        int row = addTerm(term);
        this.addToRow(row, values);
    }

    public LatentSemanticIndex createIndex() {
        return new LatentSemanticIndex(documents.clone(), terms.clone(), globalWeighting, SVD.fromMatrix(this, 30));
    }

    public TermDocumentMatrix rejectAndWeight() {
        return rejectHapaxes().toLowerCase().rejectStopwords().stem().weight(LocalWeighting.TERM, GlobalWeighting.IDF);
    }

    public TermDocumentMatrix rejectHapaxes() {
        return rejectLegomena(1);
    }

    public TermDocumentMatrix rejectLegomena(int threshold) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new Index<String>(), documents);
        for (Pair<String,Vector> each: zip(terms, rows())) {
            if (each.snd.used() <= threshold) continue;
            tdm.addTerm(each.fst, each.snd);
        }
        return tdm;
    }

    public TermDocumentMatrix rejectStopwords() {
        return rejectStopwords(Stopwords.BASIC_ENGLISH);
    }

    public TermDocumentMatrix rejectStopwords(Stopwords stopwords) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new Index<String>(), documents);
        for (Pair<String,Vector> each: zip(terms, rows())) {
            if (stopwords.contains(each.fst)) continue;
            tdm.addTerm(each.fst, each.snd);
        }
        return tdm;
    }

    public TermDocumentMatrix stem() {
        return stem(new PorterStemmer());
    }

    public TermDocumentMatrix stem(Stemmer stemmer) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new Index<String>(), documents);
        for (Pair<String,Vector> each: zip(terms, rows())) {
            tdm.addTerm(stemmer.stem(each.fst), each.snd);
        }
        return tdm;
    }

    public Terms terms() {
        Terms bag = new Terms();
        for (Pair<String,Vector> each: zip(terms, rows())) {
            bag.add(each.fst, (int) each.snd.sum());
        }
        return bag;
    }

    private int termSize() {
        return terms.size();
    }

    public TermDocumentMatrix toLowerCase() {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new Index<String>(), documents);
        for (Pair<String,Vector> each: zip(terms, rows())) {
            tdm.addTerm(each.fst.toString().toLowerCase(), each.snd);
        }
        return tdm;
    }

    @Override
    public String toString() {
        return String.format("TermDocumentMatrix (%d terms, %d documents)", rowSize(), columnSize());
    }

    public TermDocumentMatrix weight(LocalWeighting localWeighting, GlobalWeighting globalWeighting) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(this.terms, this.documents);
        tdm.globalWeighting = new double[termSize()];
        for (Each<Vector> row: withIndex(rows())) {
            double global = tdm.globalWeighting[row.index] = globalWeighting.weight(row.element);
            for (Entry column: row.element.entries()) {
                tdm.put(row.index, column.index, localWeighting.weight(column.value) * global);
            }
        }
        return tdm;
    }

}
