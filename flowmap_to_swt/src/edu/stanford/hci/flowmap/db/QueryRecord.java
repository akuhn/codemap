package edu.stanford.hci.flowmap.db;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

/**
 * Provides a complete data model for a flowmap
 *
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class QueryRecord extends AbstractTableModel {

	
	/*************************************************************************
	 * Static methods and variables 
	 ************************************************************************/
	private static int queryId = 0;
	
	public static Integer getNextQueryId() {
		return new Integer(queryId++);
	}
	
	/*************************************************************************
	 * Class Definition
	 ************************************************************************/
	protected QueryRow m_sourceRow;
	protected ArrayList<QueryRow> m_flowRows;
	
	protected RowSchema rowSchema;
	
	protected HashMap<String, MinMaxTotal> name2MinMaxTotals;
		
	// the id of the query that produced this map
	protected Integer m_id;
		
	public QueryRecord() {
		m_id = getNextQueryId();
		m_flowRows = new ArrayList<QueryRow>();
		name2MinMaxTotals = new HashMap<String, MinMaxTotal>();
		clear();
		
	}
	
	public QueryRecord(QueryRecord copy) {
		m_id = copy.getId();
		m_flowRows = new ArrayList<QueryRow>();
		name2MinMaxTotals = new HashMap<String, MinMaxTotal>();
		clear();
		
		this.setRowSchema(copy.getRowSchema());
		
		m_sourceRow = new QueryRow(copy.getSourceRow());
		for(QueryRow copyRow : copy.getRows()) {
			this.addFlowRow(new QueryRow(copyRow));
		}
		
		this.rowsDone();
	}
	
	
	public Integer getId() {
		return m_id;
	}
	
	public int size() {
		return m_flowRows.size();
	}
	
	public void clear() {
		name2MinMaxTotals.clear();
		m_flowRows.clear();
		rowSchema = null;
		m_sourceRow = null;
	}

	public void addFlowRow(QueryRow row) {
		assert (rowSchema != null);
		assert (rowSchema == row.getRowSchema());
		assert row.getQueryId() == m_id;
		m_flowRows.add(row);
	}
	
	public Collection<QueryRow> getRows() {
		return m_flowRows;
	}
	
	public Iterator getRowsIterator() {
		return m_flowRows.iterator();
	}
	
	public QueryRow getSourceRow() {
		return m_sourceRow;
	}	
	
	public void setSourceRow(QueryRow srcRow) {
		assert srcRow.getQueryId() == m_id;
		m_sourceRow = srcRow;
		//System.out.println("QR.sourceRow: " + srcRow.getRowSchema());
	}
	
	public String getSourceName() {
		return m_sourceRow.getName();
	}
	
	public Point2D getSourcePt() {
		return m_sourceRow.getLocation();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();

		Iterator i = m_flowRows.iterator();
		int count = 1;
		while (i.hasNext()) {
			buf.append(count);
			buf.append(" ");
			buf.append(i.next());
			buf.append("\n");
			count++;
		}
		
		return buf.toString();
	}
	
	/**
	 * Clears the min max totals
	 *
	 */
	private void clearMinMaxTotals() {
		for(String s : name2MinMaxTotals.keySet()) {
			MinMaxTotal mmt = name2MinMaxTotals.get(s);
			mmt.clear();
		}
	}
	
	/**
	 * Call this when done adding rows. Computes statistics about the min
	 * max and totals for the rows.
	 */
	public void rowsDone() {
		//System.out.println("RowsDone for id:"+m_id);
		//System.out.println("QueryRecord rowSchema - " + rowSchema);
		
		// make sure we are not aggregating over old data
		clearMinMaxTotals();
		
		Collection<ColumnSchema> numTypes = rowSchema.getNumberTypes();
		for(QueryRow row : m_flowRows) {
			//System.out.println("row: " + row);
			//System.out.println("srcrow: " + m_sourceRow.getRowSchema());
			//row.printOutData();
			if (row.getName().equals(m_sourceRow.getName()))
				continue;
			for(ColumnSchema cs : numTypes) {
				MinMaxTotal mmt = name2MinMaxTotals.get(cs.columnName);
				mmt.update((Double)row.getInfo(cs.columnName));
			}
		}
	}
	
	/**
	 * @param columnName name of the column to get value for
	 * @return the smallest value among all of the flow rows. uses linear search
	 */
	public double getMinValue(String columnName) {
		MinMaxTotal mmt = name2MinMaxTotals.get(columnName);
		return mmt.getMin();
	}
	
	/**
	 * @param columnName name of the column to get value for
	 * @return the largest value among all of the flow rows
	 */
	public double getMaxValue(String columnName) {
		MinMaxTotal mmt = name2MinMaxTotals.get(columnName);
		return mmt.getMax();
	}
	
	/**
	 * @param columnName name of the column to get value for
	 * @return the largest value among all of the flow rows
	 */
	public double getTotalValue(String columnName) {
		//System.out.println("getTotalValue for id:"+m_id);
		
		//System.out.println("QueryRecord.getTotalValue columnName:"+columnName);
		MinMaxTotal mmt = name2MinMaxTotals.get(columnName);
		
		//System.out.println("QueryRecord getTotalValue n2MMT:"+ name2MinMaxTotals.size());
		//System.out.println("QueryRecord getTotalValue RowSchema"+ rowSchema);
		return mmt.getTotal();
	}

	/***********************************************************************
	 * AbstractTableModel methods
	 ***********************************************************************/
	
	public int getColumnCount() {
		if (rowSchema == null)
			return 0;
		else
			return rowSchema.size();
	}

	public int getRowCount() {
		return m_flowRows.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		QueryRow row = (QueryRow) m_flowRows.get(rowIndex);
		Object obj = row.getInfoAt(columnIndex);
		return obj;
	}


	public RowSchema getRowSchema() {
		return rowSchema;
	}
	
	/**
	 * We allow rowSchema to be set, on the chance that in the future we want to
	 * automatically infer types to allow people to do filters and such. 
	 * @param rowSchema
	 */
	public void setRowSchema(RowSchema rowSchema) {
		assert (rowSchema != null);
		this.rowSchema = rowSchema;
		
		name2MinMaxTotals.clear();
		//for each of the row schema that are numbers, we store max min values for them
		Collection<ColumnSchema> numTypes = rowSchema.getNumberTypes();
		for (ColumnSchema cs : numTypes ) {
			MinMaxTotal mmt = new MinMaxTotal(cs.columnName);
			name2MinMaxTotals.put(cs.columnName, mmt);
		}
		
	}
	
	/**
	 * Records the min, max, and total values for a particular column
	 * @author dphan
	 *
	 */
	public class MinMaxTotal {
		// the name of the schema this is storing the values for
		private String name;
		private double low, high, total;
		
		public MinMaxTotal(String name) {
			this.name = name;
			clear();
		}
		
		public void clear() {
			low = Double.MAX_VALUE;
			high = Double.MIN_VALUE;
			total = 0;
		}
		
		public String getName() {
			return name;
		}
		
		public double getMin() {
			return low;
		}
		
		public double getMax() {
			return high;
		}
		
		public double getTotal() {
			return total;
		}
		
		public void update(double val) {
			if (val < low) {
				low = val;
			} 
			
			if (val > high) {
				high = low;
			}
			
			total += val;
		}
		
	}
}
