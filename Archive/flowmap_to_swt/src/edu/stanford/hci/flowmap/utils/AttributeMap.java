package edu.stanford.hci.flowmap.utils;

import java.util.HashMap;
import java.util.Set;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class AttributeMap {
	
	protected HashMap<String, Object> m_map;
	
	public AttributeMap() {
		m_map = new HashMap<String, Object>();
	}
	
	public Set<String> keys() {
		return m_map.keySet();
	}
	
	public boolean contains(String key) {
		return m_map.containsKey(key);
	}
	
	private Object put(String key, Object value) {
		return m_map.put(key, value);
	}
	
	public Object putDouble(String key, double value) {
		return put(key, new Double(value));
	}
	
	public Object putBoolean(String key, boolean value) {
		if (value)
			return put(key, Boolean.TRUE);
		else
			return put(key, Boolean.FALSE);
	}
	
	public Object putString(String key, String value) {
		return put(key, value);
	}
	
	public Object get(String key) {
		return m_map.get(key);
	}
	
	public double getDouble(String key) {
		return ((Double)get(key)).doubleValue();
	}
	
	public boolean getBoolean(String key) {
		return ((Boolean)get(key)).booleanValue();
	}
	
	public String getString(String key) {
		return (String)get(key);
	}
	
}
