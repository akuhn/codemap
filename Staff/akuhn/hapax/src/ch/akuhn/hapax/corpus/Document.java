package ch.akuhn.hapax.corpus;

import java.io.File;


public class Document {

    private Object file;

    public Document(Object file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return (file instanceof File ? ((File) file).getName() : file.toString());
    }

}
