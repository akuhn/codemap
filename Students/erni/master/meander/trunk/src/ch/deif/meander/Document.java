package ch.deif.meander;

import java.util.Collection;

public class Document {

    public final String label;
    public final Collection<String> terms;
    public final String version;
    
    public Document(String label, Collection<String> terms, String version) {
        this.label = label;
        this.terms = terms;
        this.version = version;
    }
    
    public Document(String label, Collection<String> terms) {
        this(label, terms, "0.0.0");
    }    
    
}
