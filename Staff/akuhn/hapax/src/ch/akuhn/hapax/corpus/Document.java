package ch.akuhn.hapax.corpus;

import java.io.File;
import java.util.Collection;


public class Document {

    public Object handle;
    public final Terms terms;

    public Document() {
        this(null, new Terms());
    }
    
    public Document(File file) {
        this.handle = file;
        this.terms = new Terms(file);
    }
    
    public Document(Object handle, Collection<String> terms) {
        this.handle = handle;
        this.terms = new Terms(terms);
    }

    @Override
    public String toString() {
        return (handle instanceof File ? ((File) handle).getName() : handle.toString());
    }

}
