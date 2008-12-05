package ch.akuhn.hapax;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.akuhn.util.PrintOn;


public class SparseMatrix {

	private int columns;
	private List<SparseVector> rows;
	
	public SparseMatrix(int rows, int columns) {
		this.columns = columns;
		this.rows = new ArrayList<SparseVector>(rows);
		for (int n = 0; n < rows; n++) addRow();
	}
	
	protected int addColumn() {
		columns++;
		for (SparseVector each: rows) each.resizeTo(columns);
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

    public double add(int row, int column, double sum) {
        return rows.get(row).add(column,sum);
    }
	
    public void put(int row, int column, double value) {
        rows.get(row).put(column,value);
    }

    public double get(int row, int column) {
        return rows.get(row).get(column);
    }
    
    public int used() {
        int used = 0;
        for (SparseVector each: rows) used += each.used();
        return used;
    }
    
    public double density() {
        return ((double) used()) / rowSize() / columnSize(); 
    }
    
    /** @see http://tedlab.mit.edu/~dr/svdlibc/SVD_F_ST.html */
    public void storeSparseOn(Appendable appendable) {
        PrintOn out = new PrintOn(appendable);
        out.print(this.rowSize()).space();
        out.print(this.columnSize()).space();
        out.print(this.used()).cr();
        for (int col = 0; col < columnSize(); col++) {
            PrintOn buf = new PrintOn();
            int used = 0;
            for (int row = 0; row < rowSize(); row++) {
                double value = get(row,col);
                if (value == 0) continue;
                buf.print(row).space().print(value).cr();
                used++;
            }
            out.print(used).cr();
            out.print(buf);
        }
    }
    
    public void storeBinaryOn(ByteBuffer out) {
        out.putInt(this.rowSize());
        out.putInt(this.columnSize());
        out.putInt(this.used());
        for (int col = 0; col < columnSize(); col++) {
            ByteBuffer mark = out.slice();
            out.putInt(-1);
            int used = 0;
            for (int row = 0; row < rowSize(); row++) {
                double value = get(row,col);
                if (value == 0) continue;
                out.putInt(row);
                out.putFloat((float) value);
                used++;
            }
            mark.putInt(used);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SparseMatrix && rows.equals(((SparseMatrix) obj).rows);
    }

    @Override
    public int hashCode() {
        return rows.hashCode();
    }
    
    
    
}
