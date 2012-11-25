package org.codemap;


public class HillShading {

    private double[][] hillShading;

    public HillShading(double[][] hillShading) {
        this.hillShading = hillShading;
    }

    public double[][] asDoubleArray() {
        return hillShading;
    }

    public int getSize() {
        return hillShading.length;
    }

}
