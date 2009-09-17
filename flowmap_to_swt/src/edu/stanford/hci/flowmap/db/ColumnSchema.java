package edu.stanford.hci.flowmap.db;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class ColumnSchema {
	public static final int NUMBER = 0;
	public static final int STRING = 1;
	public static final int DATE = 2;
	//public static final int POINT = 3;
	
	public String columnName = null;
	public int columnType = STRING;
	public int columnIndex = 0;		//the index in the flow row, which is started at 0, while the columns in the db start at 1
	
	public ColumnSchema(String colName, int type, int colNum){
		columnName = colName;
		columnType = type;
		columnIndex = colNum;
	}
	
	public String toString() {
		return "[" + columnName + " t:" + columnType + " i:" + columnIndex + "]";
	}
}
