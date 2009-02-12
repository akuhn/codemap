package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;


public class Location {

    public double x, y;
    public double height;
    public Document document;
    
    public Location(double xNormed, double yNormed, double height) {
        this.x = xNormed;
        this.y = yNormed;
        this.height = height;
    }
    
}
