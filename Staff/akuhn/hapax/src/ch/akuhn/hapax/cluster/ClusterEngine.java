package ch.akuhn.hapax.cluster;

import static ch.akuhn.util.Interval.range;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;

public class ClusterEngine<T> implements Runnable {

    private static final int DONE = -1;

    private List<Dendrogram<T>> clusters;
    int pending, found_a, found_b;
    private ClusterEngineRow[] rows;
    private boolean similarity;
    private double threshold;

    private int[] todos;

    public ClusterEngine(List<T> elements, Distance<T> dist) {
        similarity = dist instanceof Similarity;
        init_clusters(elements);
        init_rows(elements, dist);
        init_todos();
        pending = todos.length;
    }

    public Dendrogram<T> dendrogram() {
        this.run();
        Dendrogram<T> root = null;
        for (int todo: todos) {
            if (todo == DONE) continue;
            assert root == null;
            root = clusters.get(todo);
        }
        return root;
    }

    private void findMinimum() {
        double min = Distance.INFINITY;
        for (int todo: todos) {
            if (todo == DONE) continue;
            ClusterEngineRow row = rows[todo];
            if (row.min() < min) {
                threshold = min = row.min();
                found_a = todo;
                found_b = row.found();
            }
        }
    }

    private double get(int row, int column) {
        if (row == column) return 0;
        return row > column ? rows[row].get(column) : rows[column].get(row);
    }

    private void init_clusters(List<T> elements) {
        this.clusters = new ArrayList<Dendrogram<T>>();
        for (T each: elements)
            this.clusters.add(new Dendrogram.Leaf<T>(each));
    }

    private void init_rows(List<T> elements, Distance<T> dist) {
        this.rows = new ClusterEngineRow[elements.size()];
        for (int row: range(rows.length)) {
            T element = elements.get(row);
            double[] values = new double[row];
            for (int col: range(values.length)) {
                values[col] = dist.dist(element, elements.get(col));
            }
            rows[row] = new ClusterEngineRow(values);
        }
    }

    private void init_todos() {
        todos = range(rows.length).asArray();
    }

    private double linkage(int todo) {
        return min(get(found_a, todo), get(found_b, todo));
    }

    private void mergeClusters() {
        todos[found_b] = DONE;
        for (int todo: todos) {
            if (todo == DONE) continue;
            put(found_a, todo, linkage(todo));
            unset(found_b, todo);
        }
        clusters.set(found_a, clusters.get(found_a)
                .merge(clusters.get(found_b), similarity ? 1 - threshold : threshold));
        clusters.set(found_b, null);
        pending--;
    }

    private double put(int row, int column, double min) {
        if (row == column) return 0;
        return row > column ? rows[row].set(column, min) : rows[column].set(row, min);
    }

    public void run() {
        while (pending > 1) {
            findMinimum();
            mergeClusters();
        }
    }

    private void unset(int row, int column) {
        if (row == column) return;
        if (row > column) rows[row].unset(column);
        else rows[column].unset(row);
    }

}

class ClusterEngineRow {

    private static final int NULL = -1;

    private int found = NULL;
    private double min = Distance.INFINITY;
    private final double[] values;

    public ClusterEngineRow(double[] values) {
        this.values = values;
        if (values.length == 0) found = 0;
    }

    public int found() {
        if (found == NULL) update();
        return found;
    }

    public double get(int index) {
        return values[index];
    }

    public double min() {
        if (found == NULL) update();
        return min;
    }

    public double set(int index, double value) {
        return values[index] = value;
    }

    public void unset(int index) {
        values[index] = Distance.INFINITY;
        if (index == found) found = NULL;
    }

    private void update() {
        min = Distance.INFINITY;
        for (int n: range(values.length)) {
            if (values[n] < min) {
                min = values[n];
                found = n;
            }
        }
    }

}