package ch.akuhn.hapax.index;

import static ch.akuhn.util.Each.withIndex;
import static ch.akuhn.util.Pair.zip;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
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
import ch.akuhn.util.PrintOn;
import ch.akuhn.util.Bag.Count;


public class TermDocumentMatrix extends Corpus {

    private Index<String> terms; // rows
    private Index<Document> documents; // columns
    private SparseMatrix matrix;
    private double[] globalWeighting;

    public TermDocumentMatrix() {
        this.matrix = new SparseMatrix(0, 0);
        this.terms = new Index<String>();
        this.documents = new Index<Document>();
    }
    
    private TermDocumentMatrix(Index<String> terms, Index<Document> documents) {
        this.matrix = new SparseMatrix(terms.size(), documents.size());
        this.terms = terms.clone();
        this.documents = documents.clone();
    }

    private int indexTerm(String term) {
        int index = terms.add(term);
        if (index == matrix.rowSize()) matrix.addRow();
        return index;
    }

    private void addTerm(String term, Vector values) {
        int row = indexTerm(term);
        matrix.addToRow(row, values);
    }

    public LatentSemanticIndex createIndex() {
        return this.createIndex(23); //TODO magic number
    }

    public LatentSemanticIndex createIndex(int dimensions) {
        return new LatentSemanticIndex(terms, documents, globalWeighting, new SVD(matrix, dimensions));
    }

    public TermDocumentMatrix rejectAndWeight() {
        return rejectHapaxes().toLowerCase().rejectStopwords().stem().weight(LocalWeighting.TERM, GlobalWeighting.IDF);
    }

    public TermDocumentMatrix rejectHapaxes() {
        return rejectLegomena(1);
    }

    public TermDocumentMatrix rejectLegomena(int threshold) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new Index<String>(), documents);
        for (Pair<String,Vector> each: zip(terms, matrix.rows())) {
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
        for (Pair<String,Vector> each: zip(terms, matrix.rows())) {
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
        for (Pair<String,Vector> each: zip(terms, matrix.rows())) {
            tdm.addTerm(stemmer.stem(each.fst), each.snd);
        }
        return tdm;
    }

    public Terms termBag() {
        Terms bag = new Terms();
        for (Pair<String,Vector> each: zip(terms, matrix.rows())) {
            bag.add(each.fst, (int) each.snd.sum());
        }
        return bag;
    }

    public TermDocumentMatrix toLowerCase() {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new Index<String>(), documents);
        for (Pair<String,Vector> each: zip(terms, matrix.rows())) {
            tdm.addTerm(each.fst.toString().toLowerCase(), each.snd);
        }
        return tdm;
    }

    @Override
    public String toString() {
        return String.format("TermDocumentMatrix (%d terms, %d documents)", termSize(), documentSize());
    }

    public TermDocumentMatrix weight(LocalWeighting localWeighting, GlobalWeighting globalWeighting) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(this.terms, this.documents);
        tdm.globalWeighting = new double[terms.size()];
        for (Each<Vector> row: withIndex(matrix.rows())) {
            double global = tdm.globalWeighting[row.index] = globalWeighting.weight(row.element);
            for (Entry column: row.element.entries()) {
                tdm.matrix.put(row.index, column.index, localWeighting.weight(column.value) * global);
            }
        }
        return tdm;
    }


//    @Override
//    public void addDocument(String name, String version,
//            Bag<String> terms) {
//        Document document = new Document(name, versionMap.get(name), terms);
//        document.dropTerms();
//        int column = addDocument(document);
//        for (Count<String> each: terms.counts()) {
//            int row = addTerm(each.element);
//            add(row, column, each.count);
//        }
//        
//    }

    @Override
    public Iterable<Document> documents() {
        return documents;
    }

    @Override
    protected Document makeDocument(String name) {
        return new Doc(name);
    }
    
    private class Doc extends Document {

        private final int column;
        
        public Doc(String name) {
            super(name);
            this.column = documents.add(this);
            if (column == matrix.columnSize()) matrix.addColumn();
        }

        @Override
        public int termSize() {
            return terms().uniqueSize();
        }

        @Override
        public Terms terms() {
            Terms terms = new Terms();
            for (Pair<String,Vector> each: zip(terms,matrix.rows())) {
                double count = each.snd.get(column);
                terms.add(each.fst, (int) count);
            }
            return terms;
        }

        @Override
        public Document addTerms(Terms terms) {
            for (Count<String> each: terms.counts()) {
                int row = indexTerm(each.element);
                matrix.add(row, column, each.count);
            }
            return this;
        }
        
    }

    public double density() {
        return matrix.density();
    }
    
    public void storeOn(Appendable app) {
        PrintOn out = new PrintOn(app);
        out.print("# Term-Document-Matrix").cr();
        out.print(this.termSize()).cr();
        for (String term: terms) {
            out.print(term).cr();
        }
        out.print(this.documentSize()).cr();
        for (Document doc: documents) {
            out.print(doc.name()).tab().print(doc.version()).cr();
        }
        matrix.storeSparseOn(app);
    }

    @Override
    public int termSize() {
        return terms.size();
    }
    
}
