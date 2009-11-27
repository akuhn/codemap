package ch.akuhn.linalg;

import static ch.akuhn.foreach.For.range;


public class DenseVector extends Vector {

    private double unit = 0;
    private double[] values;
    
    protected DenseVector(double[] values) {
        this.values = values;
    }

    protected DenseVector(int size) {
        values = new double[size];
    }

    public double cosine(DenseVector other) {
        assert other.size() == this.size();
        double sum = 0;
        for (int n: range(values.length))
            sum += values[n] * other.values[n];
        return sum / (this.norm() * other.norm());
    }


    @Override
    public double get(int index) {
        return values[index];
    }

    @Override
    public double norm() {
        if (unit != 0) return unit; // FIXME should purge cache on edit
        double qsum = 0;
        for (double value: values)
            qsum += value * value;
        if (qsum == 0) qsum = 1;
        return unit = Math.sqrt(qsum);
    }

    @Override
    public double put(int index, double value) {
        return values[index] = value;
    }

    @Override
    public int size() {
        return values.length;
    }

    public Vector times(double scalar) {
        double[] times = new double[values.length];
        for (int n: range(values.length))
            times[n] = values[n] * scalar;
        return new DenseVector(times);
    }

}
