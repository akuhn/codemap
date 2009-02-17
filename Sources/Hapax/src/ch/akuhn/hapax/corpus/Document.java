package ch.akuhn.hapax.corpus;

import java.util.Collection;


public class Document {

    private String name;
    private Terms terms;
    private int termSize;
    private VersionNumber version;

    public Document(String name, Collection<String> terms) {
        this(name, null, terms);
    }

    public Document(String name, VersionNumber version, Collection<String> terms) {
        this.name = name;
        this.terms = new Terms(terms);
        this.termSize = this.terms.size();
        this.version = version;
    }

    @Override
    public String toString() {
        return name;
    }

    public String name() {
        return name;
    }

    public Terms terms() {
        return terms;
    }

    public int termSize() {
        return termSize;
    }

    public VersionNumber version() {
        return version;
    }

}
