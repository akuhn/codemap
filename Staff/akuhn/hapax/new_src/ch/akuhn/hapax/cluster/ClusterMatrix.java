package ch.akuhn.hapax.cluster;

import java.util.ArrayList;
import java.util.List;


public class ClusterMatrix<T> {

    private static class Row {

        private int found = -1;
        private double min;
        private final double[] values;

        public Row(double[] values) {
            this.values = values;
        }

        public int found() {
            if (found == -1) update();
            return found;
        }

        public double min() {
            if (found == -1) update();
            return min;
        }

        public void unset(int index) {
            values[index] = Double.POSITIVE_INFINITY;
            if (index == found) found = -1;
        }

        private void update() {
            min = Double.POSITIVE_INFINITY;
            for (int n = 0; n < values.length; n++) {
                if (values[n] < min) {
                    min = values[n];
                    found = n;
                }
            }
        }

    }

    private List<Dendro<T>> elements;
    private int[] indices;
    int p, row0, column0;
    private Row[] rows;

    private double threshold;

    public ClusterMatrix(List<T> elements, Similarity<T> sim) {
        this.elements = new ArrayList<Dendro<T>>();
        for (T each : elements)
            this.elements.add(new Dendro.Leaf<T>(each));

        this.rows = new Row[elements.size()];
        for (int row = 0; row < rows.length; row++) {
            T element = elements.get(row);
            double[] values = new double[row];
            for (int col = 0; col < values.length; col++) {
                values[col] = 1 - sim.similarity(element, elements.get(col));
            }
            rows[row] = new Row(values);
        }
        indices = new int[rows.length];
        for (int n = 0; n < rows.length; n++)
            indices[n] = n;
        p = indices.length;
    }

    public Dendro<T> cluster() {
        while (p > 1) {
            findMinimum();
            System.out.printf("%d\t%d\n", row0, column0);
            mergeClusters();
        }
        for (int index : indices) {
            if (index == -1) continue;
            return elements.get(index);
        }
        throw new Error();
    }

    private void findMinimum() {
        double min = Double.POSITIVE_INFINITY;
        for (int index : indices) {
            if (index < 1) continue;
            Row row = rows[index];
            if (row.min() < min) {
                threshold = min = row.min();
                row0 = index;
                column0 = row.found();
            }
        }
    }

    private double get(int row, int column) {
        if (row == column) return 0;
        return row > column ? rows[row].values[column] : rows[column].values[row];
    }

    private void mergeClusters() {
        indices[column0] = -1;
        for (int index : indices) {
            if (index == -1) continue;
            put(row0, index, Math.min(get(row0, index), get(column0, index)));
        }
        for (int index : indices) {
            if (index == -1) continue;
            unset(index, column0);
        }
        elements.set(row0, elements.get(row0).merge(elements.get(column0), threshold));
        elements.set(column0, null);
        p--;
    }

    private double put(int row, int column, double min) {
        if (row == column) return 0;
        return row > column ? (rows[row].values[column] = min) : (rows[column].values[row] = min);
    }

    private void unset(int row, int column) {
        if (row == column) return;
        if (row > column) rows[row].unset(column);
        else rows[column].unset(row);
    }

}
