package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Each.withIndex;
import static ch.akuhn.util.Get.shuffle;
import static ch.akuhn.util.Get.take;
import static ch.akuhn.util.Interval.range;
import static ch.akuhn.util.Times.repeat;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.Each;
import ch.akuhn.util.Files;
import ch.akuhn.util.PrintOn;
import ch.akuhn.util.Throw;
import ch.akuhn.util.Times;

public class SparseMatrix extends Matrix {

    public static void randomStoreSparseOn(int rowSize, int columnSize, double density, Appendable app) {
        int num = (int) (rowSize * density);
        Random rand = new Random();
        PrintOn out = new PrintOn(app);
        out.print(rowSize).space().print(columnSize).space().print(num * columnSize).cr();
        for (@SuppressWarnings("unused")
        int times: repeat(columnSize)) {
            out.print(num).cr();
            for (int element: take(num, shuffle(range(rowSize)))) {
                out.print(element).space().print(rand.nextInt(20)).cr();
            }
        }
        Files.close(app);
    }

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

    public void randomFill(double density) {
        int rowSize = rowSize();
        int columnSize = columnSize();
        int num = (int) (rowSize * columnSize * density);
        Random rand = new Random();
        for (@SuppressWarnings("unused")
        int times: repeat(num)) {
            put(rand.nextInt(rowSize), rand.nextInt(columnSize), rand.nextFloat() * 20);
        }
    }

    @Override
    public Iterable<Vector> rows() {
        return rows;
    }

    @Override
    public int rowSize() {
        return rows.size();
    }

    protected void setRow(int row, Vector values) {
        rows.set(row, values);
    }

    public void storeBinaryOn(DataOutput out) throws IOException {
        out.writeInt(this.rowSize());
        out.writeInt(this.columnSize());
        out.writeInt(this.used());
        for (Vector row: rows) {
            out.writeInt(row.used());
            for (Entry each: row.entries()) {
                out.writeInt(each.index);
                out.writeFloat((float) each.value);
            }
        }
        Files.close(out);
    }

    public void storeBinaryOn(String fname) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fname));
            storeBinaryOn(out);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    /** @see http://tedlab.mit.edu/~dr/svdlibc/SVD_F_ST.html */
    public void storeSparseOn(Appendable appendable) {
        // this stores the transposed matrix, but as we will transpose it again
        // when reading it, this can be done without loss of generality.
        PrintOn out = new PrintOn(appendable);
        out.print(this.columnSize()).space();
        out.print(this.rowSize()).space();
        out.print(this.used()).cr();
        for (Vector row: rows) {
            out.print(row.used()).cr();
            for (Entry each: row.entries()) {
                out.print(each.index).space().print(each.value).cr();
            }
        }
        Files.close(appendable);
    }

    public void storeSparseOn(String fname) {
        storeSparseOn(Files.openWrite(fname));
    }

    @Override
    public int used() {
        int used = 0;
        for (Vector each: rows)
            used += each.used();
        return used;
    }

}
