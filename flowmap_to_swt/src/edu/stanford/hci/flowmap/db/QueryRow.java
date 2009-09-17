package edu.stanford.hci.flowmap.db;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Stores a row from a SQL query. 
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class QueryRow implements Serializable {
	
	/*************************************************************************
	 * Static methods and variables 
	 ************************************************************************/

	protected final static Integer defaultID = new Integer(-1);
	
	/**
	 * @param name the row's name
	 * @param x x-location
	 * @param y y-location
	 * @return a QueryRow at the DefaultLayer (-1)
	 */
	
	public static QueryRow getDefaultRow(String name, double x, double y, double val) {
		return getRowWithID(name, x, y, val, defaultID);
	}

	/**
	 * @param name the row's name
	 * @param x x-location
	 * @param y y-location
	 * @return a QueryRow at the DefaultLayer (-1)
	 */
	
	public static QueryRow getRowWithID(String name, double x, double y, double val, Integer id) {
		QueryRow row = new QueryRow(RowSchema.getDefaultRowSchema(), id);
		row.setInfo(RowSchema.DEFAULT_NAME, name);
		row.setInfo(RowSchema.DEFAULT_VALUE, new Double(val));
		row.setInfo(RowSchema.X_POS, x);
		row.setInfo(RowSchema.Y_POS, y);
		row.usesDefaultSchema = true;
		return row;
	}
	
	/*************************************************************************
	 * Class Definition
	 ************************************************************************/
	
	protected Object[] allColumns;
	
	protected int m_numColumns;
	protected Integer m_queryId;
	
	protected RowSchema rowSchema;
	protected Point2D screenPoint;

	protected boolean usesDefaultSchema;


	/**
	 * Constructs a QueryRow object
	 * @param rowSchema specifies the mapping of column names to index
	 * @param positionLayout specifies the mapping of query rows to screen locations
	 */
	public QueryRow(RowSchema rowSchema, Integer queryId) {
		this.rowSchema = rowSchema;
		screenPoint = null;
		allColumns = new Object[rowSchema.size()];
		m_queryId = queryId;
		usesDefaultSchema = false;
		
		for(ColumnSchema cs : rowSchema.getColumns()) {
			switch(cs.columnType) {
			case ColumnSchema.DATE:
				allColumns[cs.columnIndex] = new Date();
				break;
			case ColumnSchema.NUMBER:
				allColumns[cs.columnIndex] = new Double(0);
				break;
			case ColumnSchema.STRING:
				allColumns[cs.columnIndex] = "";
				break;
			}
			
		}
	}
	
	public QueryRow(QueryRow copy) {
		this.rowSchema = copy.getRowSchema();
		screenPoint = copy.getLocationClone();
		allColumns = new Object[rowSchema.size()];
		usesDefaultSchema = copy.usesDefaultSchema;

		m_queryId = copy.getQueryId();
		
		Collection<ColumnSchema> coll = rowSchema.getColumns();
		for(ColumnSchema cs : coll) {
			Object o = copy.getInfo(cs.columnName);
			if (cs.columnType == ColumnSchema.STRING) {
				String s = (String)o;
				allColumns[cs.columnIndex] = new String(s);
			} else if (cs.columnType == ColumnSchema.NUMBER) {
				Double d = (Double)o;
				allColumns[cs.columnIndex] = new Double(d.doubleValue());				
			} else if (cs.columnType == ColumnSchema.DATE) {
				Date date = (Date)o;
				allColumns[cs.columnIndex] = date.clone();
			} else {
				throw new RuntimeException();
			}
		}
	}

	public Integer getQueryId() {
		return m_queryId;
	}
	
	public void setQueryId(Integer id) {
		m_queryId = id;
	}

	public void setInfo(String name, Object data) {
		allColumns[rowSchema.getIndex(name)] = data;
	}
	
	/**
	 * Notice how we cast the name to a string (in the case the row's name is a number)
	 * @return the name of the row
	 */
	public String getName() {
		return (String) (getInfo(rowSchema.getDefaultNameId())+"");
	}
	
	public double getDefaultValue() {
		Double d = (Double) getInfo(rowSchema.getDefaultValueId());
		return d.doubleValue();
	}
	
	public Object getInfo(String name) {
		return allColumns[rowSchema.getIndex(name)];
	}
	
	public Object getInfoAt(int index) {
		return allColumns[index];
	}
	
	public double getValue(String name) {
		//System.out.println("QueryRow.Get name: " + name + " index: " + rowSchema.getIndex(name));
		//System.out.println("Value there is: " + allColumns[rowSchema.getIndex(name)]);
		Object obj = allColumns[rowSchema.getIndex(name)];
		if (obj instanceof Integer)
			return ((Integer)obj).intValue();
		else if (obj instanceof Double) {
			return ((Double)allColumns[rowSchema.getIndex(name)]).doubleValue();
		} else {
			throw new RuntimeException(obj.getClass().getName());
		}
	}
	
	public double getValueAt(int index) {
		return ((Double)allColumns[index]).doubleValue();
	}
	
	public Point2D getLocation() {
		if (screenPoint == null) {
			screenPoint = rowSchema.getPositionLayout().computePostion(this);
		}
		return screenPoint;	
	}
	
	public Point2D getLocationClone() {
		if (screenPoint == null) {
			screenPoint = rowSchema.getPositionLayout().computePostion(this);
		}
		return (Point2D) getLocation().clone();
	}
	
	public void setLocation(double x, double y) {
		if (screenPoint == null) {
			screenPoint = rowSchema.getPositionLayout().computePostion(this);
		}
		screenPoint.setLocation(x,y);
	}
	
	public String toString() {
		String moreData = "";
		for(Object o : allColumns) {
			moreData += o + ",";
		}
		return moreData;
	}
	
	public RowSchema getRowSchema() {
		return rowSchema;
	}
	
	/**
	 * Used by position layout methods to decide if they need to use the default schema
	 * or the app-specific schema to get the x and y location
	 * @return true if this uses the default schema
	 */
	public boolean hasDefaultSchema() {
		return usesDefaultSchema;
	}
	
	public void printOutData() {
		for (Object o : allColumns) {
			System.out.println(o);
		}
	}
}
