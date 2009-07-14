package ch.akuhn.hapax.linalg;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.Files;
import ch.akuhn.util.PrintOn;
import ch.akuhn.util.Throw;

public abstract class Matrix {

    public double add(int row, int column, double value) {
        return put(row, column, get(row, column) + value);
    }

    public abstract Iterable<Vector> columns();

    public abstract int columnCount();

    public double density() {
        return (double) used() / size();
    }
    
    public int size() {
    	return rowCount() * columnCount();
    }

    public abstract double get(int row, int column);

    public abstract double put(int row, int column, double value);

    public abstract Iterable<Vector> rows();

    public abstract int rowCount();

    public abstract int used();

    public void storeBinaryOn(DataOutput out) throws IOException {
        out.writeInt(this.rowCount());
        out.writeInt(this.columnCount());
        out.writeInt(this.used());
        for (Vector row: rows()) {
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
        out.print(this.columnCount()).space();
        out.print(this.rowCount()).space();
        out.print(this.used()).cr();
        for (Vector row: rows()) {
            out.print(row.used()).cr();
            for (Entry each: row.entries()) {
                out.print(each.index).space().print(each.value).space();
            }
            out.cr();
        }
        out.close();
    }

    public void storeSparseOn(String fname) {
        storeSparseOn(Files.openWrite(fname));
    }

}
