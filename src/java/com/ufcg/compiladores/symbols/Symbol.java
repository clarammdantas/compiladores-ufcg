package com.ufcg.compiladores.symbols;

import com.ufcg.compiladores.type.LiteralType;

public class Symbol {

	private String identifier;
	private LiteralType type;
	
	public Symbol(String identifier, LiteralType type) {
		this.identifier = identifier;
		this.type = type;
	}
	
	public String getId() {
		return this.identifier;
	}
	
	public void setId(String newId) {
		this.identifier = newId;
	}
	
	public String getType() {
		return this.type.getName();
	}
}
