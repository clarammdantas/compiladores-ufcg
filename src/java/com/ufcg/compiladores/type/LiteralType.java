package com.ufcg.compiladores.type;

public class LiteralType {
	
	private String name;
	
	public LiteralType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
