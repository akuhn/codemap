package ch.deif.meander;

import ch.akuhn.hapax.corpus.Document;

public class Location {

    public float x, y;
    public float height;
    public Document document;

    public Location(double xNormed, double yNormed, double height) {
        this.x = (float) xNormed;
        this.y = (float) yNormed;
        this.height = (float) height;
    }

}
