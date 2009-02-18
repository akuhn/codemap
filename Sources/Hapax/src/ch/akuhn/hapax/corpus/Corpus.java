package ch.akuhn.hapax.corpus;

import ch.akuhn.util.Bag;

public interface Corpus {

    public abstract Iterable<Document> documents();

    public abstract void addDocument(String name, String version, Bag<String> terms);

}