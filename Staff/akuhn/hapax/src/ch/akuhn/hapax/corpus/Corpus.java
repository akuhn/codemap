package ch.akuhn.hapax.corpus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.util.Files;

public class Corpus {

    private Collection<Document> documents;

    public Corpus() {
        this.documents = new ArrayList<Document>();
    }

    public Iterable<Document> documents() {
        return documents;
    }

    public int documentSize() {
        return documents.size();
    }

    public void add(Document document) {
        documents.add(document);
    }

    public void scanFolder(File folder, String... extensions) {
        for (File each: Files.find(folder, extensions))
            add(new Document(each));
    }

    public Terms terms() {
        Terms terms = new Terms();
        for (Document each: documents) terms.addAll(each.terms);
        return terms;
    }

    public int termSize() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("Corpus (%d documents, %d terms)", documentSize(), termSize());
    }

}
