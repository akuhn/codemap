package ch.deif.meander;

import org.codemap.layers.Label;

public class Labeling {

    private Iterable<Label> labels;

    public Labeling(Iterable<Label> labels) {
        this.labels = labels;
    }

    public Iterable<Label> labels() {
        return labels;
    }

}
