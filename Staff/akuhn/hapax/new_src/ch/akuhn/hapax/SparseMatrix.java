package ch.akuhn.hapax;

import java.util.ArrayList;
import java.util.List;

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
		return rows() - 1; 
	}
	
	public int columns() {
		return columns;
	}
	
	public int rows() {
		return rows.size();
	}
	
}
