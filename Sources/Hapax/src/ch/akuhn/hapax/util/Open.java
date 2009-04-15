package ch.akuhn.hapax.util;

import java.io.File;

public class Open {

    public static final Resource file(File file) {
        return new FileResource(file);
    }
    
    public static final Resource file(String name) {
        return new FileResource(new File(name));
    }
    
}
