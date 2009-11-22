package ch.akuhn.matrix;

public class Vector {

    private double[] values;

    public Vector(double[] values) {
        this.values = values;
    }

    public void applyCentering() {
        double sum = 0;
        for (int i = 0; i < values.length; i++) sum += values[i];
        double mean = sum / values.length;
        for (int i = 0; i < values.length; i++) values[i] -= mean;
    }

}
