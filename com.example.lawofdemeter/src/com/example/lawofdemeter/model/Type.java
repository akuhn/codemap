package com.example.lawofdemeter.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Type {


	private Map<String,Field> fields;
	private Map<String,Method> methods;
	private Set<Type> parents;

	public Iterable<Field> fields() {
		return fields.values();
	}
	
	public Iterable<Method> methods() {
		return methods.values();
	}
	
	public Iterable<Type> parents() {
		return parents;
	}
	
	private String name;

	public Type() {
		this.fields = new HashMap<String,Field>();
		this.methods = new HashMap<String,Method>();
		this.parents = new HashSet<Type>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void putField(String name, Type type) {
		Field field = fields.get(name);
		if (field == null) fields.put(name, field = new Field());
		field.setName(name);
		field.setType(type);
	}
	
	public void removeField(String name) {
		fields.remove(name);
	}

	public void putMethod(String name, Type returnType, Type... parameterTypes) {
		Method method = methods.get(name);
		if (method == null) methods.put(name, method = new Method());
		method.setName(name);
		method.setReturnType(returnType);
		method.setParameterTypes(parameterTypes);
	}
	
	public Iterable<Type> friends() {
		Collection<Type> friends = new HashSet<Type>();
		for (Type parent: parents()) {
			for (Type friend: parent.friends()) friends.add(friend);
		}
		for (Field field: fields()) {
			friends.add(field.getType());
		}
		for (Method method: methods()) {
			for (Type friend: method.parameterTypes()) friends.add(friend);
		}
		return friends;
	}
	
}
