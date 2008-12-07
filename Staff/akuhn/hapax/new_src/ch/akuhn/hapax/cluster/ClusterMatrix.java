package ch.akuhn.hapax.cluster;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import java.util.List;


public class ClusterMatrix<T> {

    private static class Row {
        
        private final double[] values;
        private double min;
        private int found = -1;
        
        public Row(double[] values) {
            this.values = values;
        }
        
        public void unset(int index) {
            values[index] = Double.POSITIVE_INFINITY;
            if (index == found) found = -1;
        }
        
        public double min() {
            if (found == -1) update();
            return min;
        }
        
        public int found() {
            if (found == -1) update();
            return found;
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
    private Row[] rows;
    private int[] indices;
    int p, row0, column0;
    private double threshold;

    public ClusterMatrix() {
    }
    
    public ClusterMatrix(List<T> elements, Similarity<T> sim) {
        this.elements = new ArrayList<Dendro<T>>();
        for (T each: elements) this.elements.add(new Dendro.Leaf<T>(each));
        
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
        for (int n = 0; n < rows.length; n++) indices[n] = n;
        p = indices.length;
    }

    public Dendro<T> cluster() {
        while (p > 1) {
            findMinimum();
            System.out.printf("%d\t%d\n", row0, column0);
            mergeClusters();
        }
        for (int index: indices) {
            if (index == -1) continue;
            return elements.get(index);
        }
        throw new Error();
    }

    private void mergeClusters() {
        indices[column0] = -1;
        for (int index: indices) {
            if (index == -1) continue;
            put(row0,index,Math.min(get(row0,index),get(column0,index)));
        }
        for (int index: indices) {
            if (index == -1) continue;
            unset(index,column0);
        }
        elements.set(row0, elements.get(row0).merge(elements.get(column0), threshold));
        elements.set(column0, null);
        p--;
    }

    private void unset(int row, int column) {
        if ( row == column ) return;
        if (row > column) rows[row].unset(column);
        else rows[column].unset(row);
    }

    private double put(int row, int column, double min) {
        if ( row == column ) return 0;
        return row > column 
                ? (rows[row].values[column] = min)
                : (rows[column].values[row] = min);
    }

    private double get(int row, int column) {
        if( row == column) return 0;
        return row > column 
                ? rows[row].values[column]
                : rows[column].values[row];
    }

    public static int[] removeIndexFrom(int[] indices2, int column02) {
        int[] array = new int[indices2.length - 1];
        System.arraycopy(indices2, 0, array, 0, column02 - 1);
        System.arraycopy(indices2, column02, array, column02 - 1, indices2.length - column02);
        return array;
    }
    
    @Test
    public void testRemoveIndexFrom() {
        int[] array = new int[] { 1, 2, 3, 4, 5, 6, 7 };
        array = removeIndexFrom(array,3);
        assertEquals("[1, 2, 4, 5, 6, 7]", Arrays.toString(array));
        array = removeIndexFrom(array,6);
        assertEquals("[1, 2, 4, 5, 6]", Arrays.toString(array));
        array = removeIndexFrom(array,0);
        assertEquals("[2, 4, 5, 6]", Arrays.toString(array));
    }

    private void findMinimum() {
        double min = Double.POSITIVE_INFINITY;
        for (int index: indices) {
            if (index < 1) continue;
            Row row = rows[index];
            if (row.min() < min) {
                threshold = min = row.min();
                row0 = index;
                column0 = row.found();
            }
        }
    }
     
}
