package ch.deif.meander;

import java.util.Collections;

public class Location {

    public double xNormed, yNormed;
    public double height;
    public Document document;
    
    public Location(double xNormed, double yNormed, double height, String document) {
        this.xNormed = xNormed;
        this.yNormed = yNormed;
        this.height = height;
        this.document = new Document(document, Collections.<String>emptySet());
    }
    
    
}
