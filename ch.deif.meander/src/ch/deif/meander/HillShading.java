package ch.deif.meander;

import java.util.List;

import ch.deif.meander.util.SparseTrueBooleanList;

public class HillShading {

    private double[][] hillShading;
    private List<SparseTrueBooleanList> contourLines;

    public HillShading(double[][] hillShading, List<SparseTrueBooleanList> contourLines) {
        this.hillShading = hillShading;
        this.contourLines = contourLines;
    }

    public double[][] asDoubleArray() {
        return hillShading;
    }

    public List<SparseTrueBooleanList> asSparseTrueBooleanList() {
        return contourLines;
    }

}
