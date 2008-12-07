package ch.akuhn.hapax.corpus;

import java.io.File;

public class Document {

    private File file;

    public Document(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.getName();
    }
    
}
