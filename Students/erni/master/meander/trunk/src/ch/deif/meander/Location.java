package ch.deif.meander;

import java.util.Collections;

public class Location {

    public double x, y;
    public double height;
    public Document document;
    
    public Location(double x, double y, double height, String document) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.document = new Document(document, Collections.<String>emptySet());
    }
    
    
}
