package ch.akuhn.hapax.linalg;

import java.util.Iterator;

public class DenseVector extends Vector {

    private class Iter implements Iterable<Entry>, Iterator<Entry> {

        private int spot = 0;

        @Override
        public boolean hasNext() {
            return spot < values.length;
        }

        @Override
        public Iterator<Entry> iterator() {
            return this;
        }

        @Override
        public Entry next() {
            return new Entry(spot, values[spot++]);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
    private double unit = 0;
    
    private double[] values;
    
    public DenseVector(double[] values) {
        this.values = values;
    }

    public DenseVector(int size) {
        values = new double[size];
    }

    public double cosine(DenseVector other) {
        assert other.size() == this.size();
        double sum = 0;
        for (int n = 0; n < values.length; n++) sum += values[n] * other.values[n];
        return sum / (this.unit() * other.unit());
    }

    @Override
    public Iterable<Entry> entries() {
        return new Iter();
    }

    @Override
    public double get(int index) {
        return values[index];
    }
    
    @Override
    public double put(int index, double value) {
        return values[index] = value;
    }
    
    @Override
    public int size() {
        return values.length;
    }

    public Vector times(double factor) {
        double[] times = new double[values.length];
        for (int n = 0; n < values.length; n++) times[n] = values[n] * factor;
        return new DenseVector(times);
    }
    
    @Override
    public double unit() {
        if (unit != 0) return unit;
        double qsum = 0;
        for (int n = 0; n < values.length; n++) qsum += values[n] * values[n];
        if (qsum == 0) qsum = 1;
        return unit = Math.sqrt(qsum);
    }
    
}
