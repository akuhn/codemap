package ch.akuhn.codemap;
import java.io.File;


public class Document {

    public final String name;
    public final String parent;
    public final int size;

    public Document(String fullname, int size) {
        File file = new File(fullname);
        this.parent = file.getParent();
        this.name = file.getName();
        this.size = size;
    }
    
}
