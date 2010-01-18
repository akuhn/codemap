package com.example.lawofdemeter.model;

public class Field {
	
	private String name;

	private Type type;
	
	public String getName() {
		return name;
	}

	public Field() {
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
