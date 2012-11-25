package ch.akuhn.hapax.index;

import static ch.akuhn.foreach.For.withIndex;

import java.util.NoSuchElementException;

import ch.akuhn.foreach.Each;
import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.PorterStemmer;
import ch.akuhn.hapax.corpus.Stemmer;
import ch.akuhn.hapax.corpus.Stopwords;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.linalg.SVD;
import ch.akuhn.hapax.linalg.SparseMatrix;
import ch.akuhn.hapax.linalg.Vector;
import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.IntArray;
import ch.akuhn.util.Pair;
import ch.akuhn.util.Bag.Count;


public class TermDocumentMatrix extends Corpus {

    private static final int DEFAULT_DIMENSIONS = 25;

    private AssociativeList<String> documents; // columns
    private double[] globalWeightings;
    private IntArray lengthArray;
    private SparseMatrix matrix;
    private AssociativeList<String> terms; // rows


    public TermDocumentMatrix() {
        this.matrix = new SparseMatrix(0, 0);
        this.terms = new AssociativeList<String>();
        this.documents = new AssociativeList<String>();
        this.lengthArray = new IntArray();
    }

    private TermDocumentMatrix(AssociativeList<String> terms, AssociativeList<String> documents, IntArray lengthArray) {
        this.matrix = new SparseMatrix(terms.size(), documents.size());
        this.terms = terms.clone();
        this.documents = documents.clone();
        this.lengthArray = lengthArray.clone();
    }

    private void addToRow(String term, Vector values) {
        int row = indexTerm(term);
        matrix.addToRow(row, values);
    }

    @Override
    public void putDocument(String doc, Terms bag) {
        int column = this.indexDocument(doc);
        lengthArray.add(column, bag.size());
        for (Count<String> term: bag.counts()) {
            int row = this.indexTerm(term.element);
            matrix.add(row, column, term.count);
        }
    }

    @Override
    public boolean containsDocument(String doc) {
        return documents.contains(doc);
    }

    public LatentSemanticIndex createIndex() {
        return this.createIndex(DEFAULT_DIMENSIONS); 
    }

    public LatentSemanticIndex createIndex(int dimensions) {
        return new LatentSemanticIndex(terms, documents, new SVD(matrix, dimensions))
                .initializeGlobalWeightings(globalWeightings)
                .initializeDocumentLength(lengthArray.asIntArray());
    }

    public double density() {
        return matrix.density();
    }

    @Override
    public Iterable<String> documents() {
        return documents;
    }

    @Override
    public int documentCount() {
        return documents.size();
    }

    private int indexTerm(String term) {
        int index = terms.add(term);
        if (index == matrix.rowCount()) matrix.addRow();
        return index;
    }

    private int indexDocument(String doc) {
        int column = documents.add(doc);
        if (column == matrix.columnCount()) matrix.addColumn();
        return column;
    }

    public TermDocumentMatrix rejectAndWeight() {
        return toLowerCase()
        .rejectHapaxes()
        .rejectStopwords()
        .stem()
        .weight(LocalWeighting.TERM, GlobalWeighting.IDF);
    }

    public TermDocumentMatrix rejectHapaxes() {
        return rejectLegomena(1);
    }

    public TermDocumentMatrix rejectLegomena(int threshold) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents, lengthArray);
        for (Pair<String,Vector> each: termRowPairs()) {
            if (each.snd.used() <= threshold) continue;
            tdm.addToRow(each.fst, each.snd);
        }
        return tdm;
    }

    public TermDocumentMatrix rejectStopwords() {
        return rejectStopwords(Stopwords.BASIC_ENGLISH);
    }

    public TermDocumentMatrix rejectStopwords(Stopwords stopwords) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents, lengthArray);
        for (Pair<String,Vector> each: termRowPairs()) {
            if (stopwords.contains(each.fst)) continue;
            tdm.addToRow(each.fst, each.snd);
        }
        return tdm;
    }

    public TermDocumentMatrix stem() {
        return stem(new PorterStemmer());
    }

    public TermDocumentMatrix stem(Stemmer stemmer) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents, lengthArray);
        for (Pair<String,Vector> each: termRowPairs()) {
            tdm.addToRow(stemmer.stem(each.fst), each.snd);
        }
        return tdm;
    }

    //    public void storeOn(Appendable app) {
    //        PrintOn out = new PrintOn(app);
    //        out.print("# Term-Document-Matrix").cr();
    //        out.print(this.termCount()).cr();
    //        for (String term: terms) {
    //            out.print(term).cr();
    //        }
    //        out.print(this.documentCount()).cr();
    //        for (Document doc: documents) {
    //            out.print(doc.name().replace(' ', '_')).tab().print(doc.version().replace(' ', '_')).cr();
    //        }
    //        matrix.storeSparseOn(app);
    //    }

    //    public void storeOn(String filename) {
    //        this.storeOn(Files.openWrite(filename));
    //    }        

    //    public static TermDocumentMatrix readFrom(Scanner scan) {
    //        TermDocumentMatrix tdm = new TermDocumentMatrix();
    //        if (scan.hasNext("#")) scan.findInLine(".*");
    //
    //        int termSize = scan.nextInt();
    //        for (int i = 0; i < termSize; i++) {
    //            String term = scan.next();
    //            tdm.indexTerm(term);
    //        }
    //        assert tdm.termCount() == termSize;
    //        
    //        int documentSize = scan.nextInt();
    //        for (int i = 0; i < documentSize; i++) {
    //            String name = scan.next();
    //            String version = scan.next();
    //            tdm.makeDocument(name, version);
    //        }
    //        assert tdm.documentCount() == documentSize;
    //
    //        tdm.matrix = SparseMatrix.readFrom(scan);
    //        
    //        return tdm;   
    //    }

    @Override
    public Terms terms() {
        Terms bag = new Terms();
        for (Pair<String,Vector> each: termRowPairs()) {
            bag.add(each.fst, (int) each.snd.sum());
        }
        return bag;
    }

    private Iterable<Pair<String,Vector>> termRowPairs() {
        return Pair.zip(terms, matrix.rows());
    }

    @Override
    public int termCount() {
        return terms.size();
    }

    public TermDocumentMatrix toLowerCase() {
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents, lengthArray);
        for (Pair<String,Vector> each: termRowPairs()) {
            tdm.addToRow(each.fst.toString().toLowerCase(), each.snd);
        }
        return tdm;
    }

    public TermDocumentMatrix weight(LocalWeighting localWeighting, GlobalWeighting globalWeighting) {
        TermDocumentMatrix tdm = new TermDocumentMatrix(this.terms, this.documents, lengthArray);
        tdm.globalWeightings = new double[terms.size()];
        for (Each<Vector> row: withIndex(matrix.rows())) {
            double global = tdm.globalWeightings[row.index] = globalWeighting.weight(row.value);
            for (Entry column: row.value.entries()) {
                tdm.matrix.put(row.index, column.index, localWeighting.weight(column.value) * global);
            }
        }
        return tdm;
    }

    @Override
    public Terms getDocument(String doc) {
        int column = documents.get(doc);
        if (column == -1) throw new NoSuchElementException();
        Terms bag = new Terms();
        for (Pair<String,Vector> each: termRowPairs()) {
            int count = (int) each.snd.get(column);
            bag.add(each.fst, count);
        }
        return bag;
    }

    //    public TermDocumentMatrix copyUpto(String version, String[] versions) {
    //        TermDocumentMatrix copy = new TermDocumentMatrix();
    //        for (String each: versions) {
    //            for (Document doc: this.documents()) {
    //                if (doc.version().equals(version)) {
    //                    copy.makeDocument(doc.name(), doc.version()).addTerms(doc.terms());
    //                }
    //            }
    //            if (version.equals(each)) return copy;
    //        }
    //        throw new Error();
    //    }

    public SparseMatrix matrix() {
        return matrix;
    }

    public int[] getAllDocumentLength() {
        int[] length = new int[documents.size()];
        for (Vector row: matrix.rows()) {
            for (Entry each: row.entries()) {
                length[each.index] += each.value;
            }
        }
        return length;
    }
 
    public int getDocumentLength(String doc) {
        int index = documents.get(doc);
        if (index < 0) return -1;
        return lengthArray.get(index);
    }
    
}
