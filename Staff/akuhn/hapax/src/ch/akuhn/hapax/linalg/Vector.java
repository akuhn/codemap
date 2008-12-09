package ch.akuhn.hapax.linalg;

public abstract class Vector {

    public final class Entry {

        public final int index;
        public final double value;

        public Entry(int index, double value) {
            this.index = index;
            this.value = value;
        }

    }

    public double add(int index, double value) {
        return put(index, get(index) + value);
    }

    public double density() {
        return ((double) used()) / size();
    }

    public abstract Iterable<Entry> entries();

    public abstract double get(int index);

    public abstract double put(int index, double value);

    public abstract int size();

    public double sum() {
        double sum = 0;
        for (Entry each : entries())
            sum += each.value;
        return sum;
    }

    public double unit() {
        double sum = 0;
        for (Entry each : entries())
            sum += each.value * each.value;
        return Math.sqrt(sum);
    }

    public int used() {
        int count = 0;
        for (Entry each : entries())
            if (each.value != 0) count++;
        return count;
    }

}
