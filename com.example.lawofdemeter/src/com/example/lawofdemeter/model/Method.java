package com.example.lawofdemeter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Method {

	private String name;

	private Type returnType;

	private Collection<Type> parameterTypes;
	
	public String getName() {
		return name;
	}

	public Method() {
		this.parameterTypes = new ArrayList<Type>();
	}	
	
	public Iterable<Type> parameterTypes() {
		return parameterTypes;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setParameterTypes(Type... parameterTypes) {
		this.parameterTypes = Arrays.asList(parameterTypes);
	}
	
}
