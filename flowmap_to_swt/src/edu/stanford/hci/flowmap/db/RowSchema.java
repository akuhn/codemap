package edu.stanford.hci.flowmap.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import edu.stanford.hci.flowmap.layout.DirectPositionLayout;
import edu.stanford.hci.flowmap.layout.PositionLayout;

/**
 * Stores all the column schema for a QueryRow. Every QueryRow has a copy of
 * a RowSchema object, which maps the names of the columns to a ColumnSchema object.
 * The ColumnSchema object stores the name, the data type, and the index in the QueryRow
 * object.
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class RowSchema {
	
	/*************************************************************************
	 * Static methods and variables 
	 ************************************************************************/
	
	public static final String DEFAULT_NAME = "Name";
	public static final String DEFAULT_VALUE = "Value";
	public static final String X_POS = DirectPositionLayout.X_POS;
	public static final String Y_POS = DirectPositionLayout.Y_POS;
	
	private static RowSchema defaultRowSchema;
	private static PositionLayout defaultPositionLayout;
	
	public static RowSchema getDefaultRowSchema() {
		return defaultRowSchema;
	}
	
	public static PositionLayout getDefaultPositionLayout() {
		return defaultPositionLayout;
	}

	//	 static initializer
	static {
		defaultPositionLayout = new DirectPositionLayout(null);
		defaultRowSchema = new RowSchema();
		defaultRowSchema.setPositionLayout(defaultPositionLayout);
		defaultRowSchema.addSchema(DEFAULT_NAME, ColumnSchema.STRING);
		defaultRowSchema.addSchema(DEFAULT_VALUE, ColumnSchema.NUMBER);
		defaultRowSchema.addSchema(X_POS, ColumnSchema.NUMBER);
		defaultRowSchema.addSchema(Y_POS, ColumnSchema.NUMBER);
		defaultRowSchema.setNameId(DEFAULT_NAME);
		defaultRowSchema.setValueId(DEFAULT_VALUE);
		
	}
	
	/*************************************************************************
	 * Class Definition
	 ************************************************************************/
	
	protected HashMap<String, ColumnSchema> nameToSchema;
	protected int numColumns;
	protected PositionLayout positionLayout;
	
	// stores all the ColumnSchema that are of the number type
	protected LinkedList<ColumnSchema> numberTypes;
	
	/**
	 * Variables that store the default name and value id strings
	 */
	protected String defaultName, defaultValue, defaultX, defaultY;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(ColumnSchema cs : getColumns()) {
			sb.append(cs.toString() + "\n");
		}
		sb.append("defaultName: " + defaultName + "\ndefaultVal: " + defaultValue);
		
		return sb.toString();
	}
	
	
	public RowSchema() {
		nameToSchema = new HashMap<String, ColumnSchema>();
		numberTypes = new LinkedList<ColumnSchema>();
		defaultName = defaultValue = null;
	}
	
	public Collection<ColumnSchema> getColumns() {
		return nameToSchema.values();
	}
	
	public void setPositionLayout(PositionLayout layout) {
		positionLayout = layout;
	}
	
	public PositionLayout getPositionLayout() {
		assert(positionLayout != null);
		return positionLayout;
	}
 	
	public int size() {
		return nameToSchema.size();
	}
	
	public void addSchema(String name, int type) {
		ColumnSchema schema = new ColumnSchema(name, type, numColumns);
		//System.out.println("RowSchema.addSchema: " + schema);
		nameToSchema.put(name, schema);
		numColumns += 1;
		
		if (type == ColumnSchema.NUMBER)
			numberTypes.add(schema);
	}
	
	/**
	 * @param name the name of the parameter to get the index for
	 * @return the ColumnSchema object associated with that name.
	 */
	public ColumnSchema getColumnSchema(String name) {
		return (ColumnSchema) nameToSchema.get(name);
	}
	
	/**
	 * Returns the index of a given name
	 * @param name
	 * @return
	 */
	public int getIndex(String name) {
		ColumnSchema cs = getColumnSchema(name);
		//System.out.println("RowSchema: getIndex of " + name + " with index: " + cs.columnIndex);
		return cs.columnIndex;
	}
	
	/**
	 * @return all the ColumnSchema that are of type number
	 */
	public Collection<ColumnSchema> getNumberTypes() {
		return numberTypes;
	}
	
	/**
	 * Sets the name of the ColumnSchema that serves as the name
	 * @param name
	 */
	public void setNameId(String name) {
		assert(nameToSchema.containsKey(name));
		defaultName = name;
	}
	
	/**
	 * Sets the name of the ColumnSchema that serves as the default value
	 * @param name
	 */
	public void setValueId(String name) {
		assert(nameToSchema.containsKey(name));
		defaultValue = name;
		
	}
	
	public void setXId(String name) {
		assert(nameToSchema.containsKey(name));
		defaultX = name;
	}
	
	public void setYId(String name) {
		assert(nameToSchema.containsKey(name));
		defaultY = name;
		
	}
	
	public String getDefaultNameId() {
		assert defaultName != null;
		return defaultName;
	}
	
	public String getDefaultValueId() {
		assert defaultValue != null;
		return defaultValue;
	}
	
	public String getDefaultY() {
		assert defaultY != null;
		return defaultY;
	}
	
	public String getDefaultX() {
		assert defaultX != null;
		return defaultX;
	}
}
