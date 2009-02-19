package ch.akuhn.hapax.corpus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.akuhn.util.Bag.Count;

public abstract class Corpus {

    private Map<String,Document> documentMap;
    
    public Corpus() {
        this.documentMap = new HashMap<String,Document>();
    }
    
    public Document addDocument(String name) {
        Document doc = documentMap.get(name);
        if (doc == null) documentMap.put(name, doc = makeDocument(name));
        return doc;
    }
    
    public Document addDocument(String name, String version, Terms terms) {
        return addDocument(name).version(version).addTerms(terms);
    }
    
    protected abstract Document makeDocument(String name);

    public Iterable<Count<String>> terms() {
        Terms terms = new Terms();
        for (Document doc: this.documents()) terms.addAll(doc.terms());
        return terms.counts();
    }

    public Iterable<String> versions() {
        Set<String> versions = new HashSet<String>();
        for (Document each: this.documents()) versions.add(each.version());
        return versions();
    }
    
    public Iterable<Document> documents() {
        return documentMap.values();
    }

    public int documentSize() {
        return documentMap.size();
    }

    public int termSize() {
        Terms terms = new Terms();
        for (Document doc: this.documents()) terms.addAll(doc.terms());
        return terms.uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("%s (%d documents, %d terms)",
                this.getClass().getName(),
                documentSize(),
                termSize());
    }

}