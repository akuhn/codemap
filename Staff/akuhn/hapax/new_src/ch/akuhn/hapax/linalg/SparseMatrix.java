package ch.akuhn.hapax.linalg;

import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.Files;
import ch.akuhn.util.PrintOn;
import ch.akuhn.util.Throw;
import ch.akuhn.util.query.Each;
import ch.akuhn.util.query.Times;

public class SparseMatrix
        extends Matrix {

    private int columns;
    private List<Vector> rows;

    public SparseMatrix(int rows, int columns) {
        this.columns = columns;
        this.rows = new ArrayList<Vector>(rows);
        for (int n = 0; n < rows; n++)
            addRow();
    }

    public SparseMatrix(double[][] values) {
        this.columns = values[0].length;
        this.rows = new ArrayList<Vector>(values.length);
        for (double[] each : values)
            addRow(each);
    }

    protected int addRow(double[] values) {
        rows.add(new SparseVector(values));
        return rowSize() - 1;
    }

    protected int addColumn() {
        columns++;
        for (Vector each : rows)
            ((SparseVector) each).resizeTo(columns);
        return columns - 1;
    }

    protected int addRow() {
        rows.add(new SparseVector(columns));
        return rowSize() - 1;
    }

    public int columnSize() {
        return columns;
    }

    public int rowSize() {
        return rows.size();
    }

    @Override
    public double add(int row, int column, double sum) {
        return rows.get(row).add(column, sum);
    }

    @Override
    public double put(int row, int column, double value) {
        return rows.get(row).put(column, value);
    }

    @Override
    public double get(int row, int column) {
        return rows.get(row).get(column);
    }

    @Override
    public int used() {
        int used = 0;
        for (Vector each : rows)
            used += each.used();
        return used;
    }

    public void storeSparseOn(String fname) {
        try {
            storeSparseOn(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(fname)))));
        } catch (FileNotFoundException ex) {
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
        for (Vector row : rows) {
            out.print(row.used()).cr();
            for (Entry each : row.entries()) {
                out.print(each.index).space().print(each.value).cr();
            }
        }
        Files.close(appendable);
    }

    public void storeBinaryOn(DataOutput out) throws IOException {
        out.writeInt(this.rowSize());
        out.writeInt(this.columnSize());
        out.writeInt(this.used());
        for (int col = 0; col < columnSize(); col++) {
            int used = 0;
            for (int row = 0; row < rowSize(); row++) {
                double value = get(row, col);
                if (value == 0) continue;
                used++;
            }
            out.writeInt(used);
            for (int row = 0; row < rowSize(); row++) {
                double value = get(row, col);
                if (value == 0) continue;
                out.writeInt(row);
                out.writeFloat((float) value);
            }
        }
        Files.close(out);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SparseMatrix
                && rows.equals(((SparseMatrix) obj).rows);
    }

    @Override
    public int hashCode() {
        return rows.hashCode();
    }

    public void storeBinaryOn(String fname) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(
                    fname));
            storeBinaryOn(out);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    public double[][] asDenseDoubleDouble() {
        double[][] dense = new double[rowSize()][columnSize()];
        for (Each<Vector> row : Each.withIndex(rows)) {
            for (Entry column : row.element.entries()) {
                dense[row.index][column.index] = column.value;
            }
        }
        return dense;
    }

    public void randomFill(double density) {
        int rowSize = rowSize();
        int columnSize = columnSize();
        long num = (long) (rowSize * columnSize * density);
        Random rand = new Random();
        for (Void each : Times.repeat(num)) {
            put(rand.nextInt(rowSize), rand.nextInt(columnSize), rand
                    .nextFloat() * 20);
        }
    }

    public static void randomStoreSparseOn(int rowSize, int columnSize,
            double density, Appendable app) {
        int num = (int) (rowSize * density);
        Random rand = new Random();
        PrintOn out = new PrintOn(app);
        out.print(rowSize).space().print(columnSize).space().print(
                num * columnSize).cr();
        for (int column = 0; column < columnSize; column++) {
            int[] pick = randomNOutOfM(rand, num, rowSize);
            out.print(num).cr();
            for (int i = 0; i < pick.length; i++) {
                out.print(pick[i]).space().print(rand.nextInt(20)).cr();
            }
        }
        Files.close(app);
    }

    private static int[] randomNOutOfM(Random rand, int num, int total) {
        int[] array = new int[total];
        for (int n = 0; n < total; n++)
            array[n] = n;
        for (int n = array.length; n > 1;) {
            int k = rand.nextInt(n--);
            int temp = array[n];
            array[n] = array[k];
            array[k] = temp;
        }
        int[] pick = new int[num];
        System.arraycopy(array, 0, pick, 0, num);
        return pick;
    }

    @Override
    public Iterable<Vector> columns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Vector> rows() {
        return rows;
    }

    protected void setRow(int row, Vector values) {
        rows.set(row, values);
    }

}
