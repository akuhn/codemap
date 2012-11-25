package com.example.lawofdemeter.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

	private Map<Object,Type> map;
	
	public Model() {
		this.map = new HashMap<Object, Type>(); 
	}
	
	public Type get(Object key) {
		Type type = map.get(key);
		if (type == null) map.put(key, type = new Type());
		return type;
	}
	
	public void remove(Object key) {
		map.remove(key);
	}
	
}
