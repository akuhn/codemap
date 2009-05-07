package ch.akuhn.hapax.corpus;

import java.util.HashSet;
import java.util.Set;


public abstract class Corpus {

    public abstract Document makeDocument(String name, String version);
    
    public Document makeDocument(String name) {
        return makeDocument(name, null);
    }
    
    public Terms terms() {
        Terms terms = new Terms();
        for (Document doc: this.documents()) terms.addAll(doc.terms());
        return terms;
    }
   

    public Iterable<String> versions() {
        Set<String> versions = new HashSet<String>();
        for (Document each: this.documents()) versions.add(each.version());
        return versions;
    }
    
    public abstract Iterable<Document> documents();

    public abstract int documentCount();

    public abstract boolean contains(Document doc);
    
    public abstract Terms terms(Document doc);
    
    public int termCount() {
        Terms terms = new Terms();
        for (Document doc: this.documents()) terms.addAll(doc.terms());
        return terms.uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("%s (%d documents, %d terms)",
                this.getClass().getName(),
                documentCount(),
                termCount());
    }

    public Document get(String name) {
        for (Document each: documents()) if (each.name().equals(name)) return each;
        throw new Error();
    }
    
    public Terms terms(String version) {
        Terms terms = new Terms();
        for (Document each: documents()) {
            if (version.equals(each.version())) {
                terms.addAll(terms(each));
            }
        }
        return terms;
    }
    
}