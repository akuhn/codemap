package ch.akuhn.hapax.index;

import static ch.akuhn.util.Each.withIndex;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.akuhn.io.chunks.ReadFromChunk;
import ch.akuhn.io.chunks.WriteOnChunk;
import ch.akuhn.util.Each;
import ch.akuhn.util.Files;
import ch.akuhn.util.Pair;
import ch.akuhn.util.PrintOn;
import ch.akuhn.util.Bag.Count;


public class TermDocumentMatrix extends Corpus {

    protected static class Doc extends Document {

        private TermDocumentMatrix owner;
        
        protected Doc(String name, String version, TermDocumentMatrix owner) {
            super(name, version);
            this.owner = owner;
        }

        @Override
        public Document addTerms(Terms terms) {
            owner.addTerms(this, terms);
            return this;
        }

        @Override
        public Corpus owner() {
            return owner;
        }

        @Override
        public Terms terms() {
            return owner.terms(this);
        }

        @Override
        public int termSize() {
            return terms().uniqueSize();
        }
        
    }
    
    private AssociativeList<Document> documents; // columns
    private double[] globalWeighting;
    private SparseMatrix matrix;
    private AssociativeList<String> terms; // rows
    
    public TermDocumentMatrix() {
        this.matrix = new SparseMatrix(0, 0);
        this.terms = new AssociativeList<String>();
        this.documents = new AssociativeList<Document>();
    }

    private TermDocumentMatrix(AssociativeList<String> terms, AssociativeList<Document> documents) {
        this.matrix = new SparseMatrix(terms.size(), documents.size());
        this.terms = terms.clone();
        this.documents = documents.clone();
    }

    private void addToRow(String term, Vector values) {
        int row = indexTerm(term);
        matrix.addToRow(row, values);
    }

    public void addTerms(Document doc, Terms bag) {
        int column = documents.get(doc);
        if (column == -1) throw new NoSuchElementException();
        for (Count<String> term: bag.counts()) {
            int row = this.indexTerm(term.element);
            matrix.add(row, column, term.count);
        }
    }

    @Override
    public boolean contains(Document doc) {
        return documents.contains(doc);
    }

    public LatentSemanticIndex createIndex() {
        return this.createIndex(20); //TODO magic number
    }

    public LatentSemanticIndex createIndex(int dimensions) {
        return new LatentSemanticIndex(terms, documents, globalWeighting, new SVD(matrix, dimensions));
    }

    public double density() {
        return matrix.density();
    }

    @Override
    public Iterable<Document> documents() {
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
    
    private int indexDocument(Document doc) {
        int column = documents.add(doc);
        if (column == matrix.columnCount()) matrix.addColumn();
        return column;
    }

    @Override
    public Document makeDocument(String name, String version) {
        Document doc = new Doc(name, version, this);
        this.indexDocument(doc);
        return doc;
    }
    
    public Document makeDocument(String name, String version, String identifier) {
    	Document doc = new Doc(name, version, this);
    	doc.setIdentifier(identifier);
    	this.indexDocument(doc);
    	return doc;
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
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents);
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
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents);
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
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents);
        for (Pair<String,Vector> each: termRowPairs()) {
            tdm.addToRow(stemmer.stem(each.fst), each.snd);
        }
        return tdm;
    }
    
    public void storeOn(Appendable app) {
        PrintOn out = new PrintOn(app);
        out.print("# Term-Document-Matrix").cr();
        out.print(this.termCount()).cr();
        for (String term: terms) {
            out.print(term).cr();
        }
        out.print(this.documentCount()).cr();
        for (Document doc: documents) {
            out.print(doc.name().replace(' ', '_')).tab().print(doc.version().replace(' ', '_')).cr();
        }
        matrix.storeSparseOn(app);
    }

    public void storeOn(String filename) {
        this.storeOn(Files.openWrite(filename));
    }        
    
    public static TermDocumentMatrix readFrom(Scanner scan) {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        if (scan.hasNext("#")) scan.findInLine(".*");

        int termSize = scan.nextInt();
        for (int i = 0; i < termSize; i++) {
            String term = scan.next();
            tdm.indexTerm(term);
        }
        assert tdm.termCount() == termSize;
        
        int documentSize = scan.nextInt();
        for (int i = 0; i < documentSize; i++) {
            String name = scan.next();
            String version = scan.next();
            tdm.makeDocument(name, version);
        }
        assert tdm.documentCount() == documentSize;

        tdm.matrix = SparseMatrix.readFrom(scan);
        
        return tdm;   
    }
        
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
        TermDocumentMatrix tdm = new TermDocumentMatrix(new AssociativeList<String>(), documents);
        for (Pair<String,Vector> each: termRowPairs()) {
            tdm.addToRow(each.fst.toString().toLowerCase(), each.snd);
        }
        return tdm;
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

    @Override
    public Terms terms(Document doc) {
        int column = documents.get(doc);
        if (column == -1) throw new NoSuchElementException();
        Terms bag = new Terms();
        for (Pair<String,Vector> each: termRowPairs()) {
            int count = (int) each.snd.get(column);
            bag.add(each.fst, count);
        }
        return bag;
    }
    
    public TermDocumentMatrix copyUpto(String version, String[] versions) {
        TermDocumentMatrix copy = new TermDocumentMatrix();
        for (String each: versions) {
            for (Document doc: this.documents()) {
                if (doc.version().equals(version)) {
                    copy.makeDocument(doc.name(), doc.version()).addTerms(doc.terms());
                }
            }
            if (version.equals(each)) return copy;
        }
        throw new Error();
    }
    
    @WriteOnChunk("TDM")
    public void storeOn(ChunkOutput chunk) throws IOException {
    	chunk.write(terms.size());
    	chunk.write(documents.size());
    	for (String each: terms) chunk.writeUTF(each);
    	for (Document each: documents) chunk.writeUTF(each.name());
    	chunk.writeChunk(matrix);
    }
    
    @ReadFromChunk("TDM")
    public TermDocumentMatrix(ChunkInput chunk) throws IOException {
    	int termCount = chunk.readInt();
    	int documentCount = chunk.readInt();
    	terms = new AssociativeList<String>();
    	for (int n = 0; n < termCount; n++) terms.add(chunk.readUTF());
    	documents = new AssociativeList<Document>();
    	for (int i = 0; i < documentCount; i++) documents.add(new Doc(chunk.readUTF(), null, this));
    	matrix = chunk.readChunk(SparseMatrix.class);
    }
    
    public SparseMatrix matrix() {
    	return matrix;
    }
    
}
