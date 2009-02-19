package ch.akuhn.hapax.corpus;


public abstract class Document {

    private String name;
    private String version;

    public Document(String name) {
        this.name = name;
    }

    public abstract Terms terms();
    
    public abstract Document addTerms(Terms terms);

    public int termSize() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
        return name;
    }

    public String name() {
        return name;
    }

    public String version() {
        return version;
    }
    
    public Document version(String version) {
        this.version = version;
        return this;
    }

}