package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Each.withIndex;

import java.util.ArrayList;
import java.util.List;

import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.Each;
import ch.akuhn.util.Times;


public class SparseMatrix extends Matrix {

    private int columns;

    private List<Vector> rows;

    public SparseMatrix(double[][] values) {
        this.columns = values[0].length;
        this.rows = new ArrayList<Vector>(values.length);
        for (double[] each: values)
            addRow(each);
    }

    public SparseMatrix(int rows, int columns) {
        this.columns = columns;
        this.rows = new ArrayList<Vector>(rows);
        for (@SuppressWarnings("unused")
        int times: Times.repeat(rows))
            addRow();
    }

    @Override
    public double add(int row, int column, double sum) {
        return rows.get(row).add(column, sum);
    }

    protected int addColumn() {
        columns++;
        for (Vector each: rows)
            ((SparseVector) each).resizeTo(columns);
        return columns - 1;
    }

    protected int addRow() {
        rows.add(new SparseVector(columns));
        return rowSize() - 1;
    }

    protected int addRow(double[] values) {
        rows.add(new SparseVector(values));
        return rowSize() - 1;
    }

    protected void addToRow(int row, Vector values) {
        Vector v = rows.get(row);
        for (Entry each: values.entries())
            v.add(each.index, each.value);
    }

    public double[][] asDenseDoubleDouble() {
        double[][] dense = new double[rowSize()][columnSize()];
        for (Each<Vector> row: withIndex(rows)) {
            for (Entry column: row.element.entries()) {
                dense[row.index][column.index] = column.value;
            }
        }
        return dense;
    }

    @Override
    public Iterable<Vector> columns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int columnSize() {
        return columns;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SparseMatrix && rows.equals(((SparseMatrix) obj).rows);
    }

    @Override
    public double get(int row, int column) {
        return rows.get(row).get(column);
    }

    @Override
    public int hashCode() {
        return rows.hashCode();
    }

    @Override
    public double put(int row, int column, double value) {
        return rows.get(row).put(column, value);
    }

    @Override
    public Iterable<Vector> rows() {
        return rows;
    }

    @Override
    public int rowSize() {
        return rows.size();
    }

    protected void setRow(int row, SparseVector values) {
        rows.set(row, values);
    }

    @Override
    public int used() {
        int used = 0;
        for (Vector each: rows)
            used += each.used();
        return used;
    }

    public void trim() {
        for (Vector each: rows) {
            ((SparseVector) each).trim();
        }
    }
    
}
