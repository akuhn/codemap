package ch.deif.meander;

import java.util.Collections;

public class Location {

    public float x, y;
    public float height;
    public Document document;
    
    public Location(float x, float y, float height, Document document) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.document = document;
    }
    
    public Location(float x, float y, float height, String document) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.document = new Document(document, Collections.<String>emptySet());
    }
    
    
}
