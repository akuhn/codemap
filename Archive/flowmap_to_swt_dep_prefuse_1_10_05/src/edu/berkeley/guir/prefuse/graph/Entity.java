package edu.berkeley.guir.prefuse.graph;

import java.util.Map;

/**
 * Interface from which all graph nodes and edges descend.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Entity {
	
	/**
	 * Get an attribute associated with this entity.
	 * @param name the name of the attribute
	 * @return the attribute value (possibly null)
	 */	
	public String getAttribute(String name);
	
	/**
	 * Get all attributes associated with this entity.
	 * @return a Map of all this entity's attributes.
	 */
	public Map getAttributes();
	
	/**
	 * Get an attribute associated with this entity.
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 */
	public void setAttribute(String name, String value);
	
	/**
	 * Sets the attribute map for this Entity. The map
	 * should contain only <code>String</code> values,
	 * otherwise an exception will be thrown.
	 * @param attrMap the attribute map
	 * @throws IllegalArgumentException is the input map
	 * contains non-<code>String</code> values.
	 */
	public void setAttributes(Map attrMap);
	
	/**
	 * Remove all attributes associated with this entity.
	 */
	public void clearAttributes();

} // end of interface Entity
