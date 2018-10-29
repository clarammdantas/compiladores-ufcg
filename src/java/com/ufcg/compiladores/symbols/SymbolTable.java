package com.ufcg.compiladores.symbols;

import java.util.HashMap;

import com.ufcg.compiladores.type.LiteralType;

public class SymbolTable {
	
	public static SymbolTable currentScope;
	public static SymbolTable root = new SymbolTable(null);
	
	private HashMap<String, String> table;
	private SymbolTable previousScope;
	
	public SymbolTable(SymbolTable previousScope) {
		this.table = new HashMap<>();
		this.previousScope = previousScope;
	}
	
	public void addSymbol(String id, String type) {
		this.table.put(id, type);
	}
	
	public boolean hasId(String id) {
		return this.table.containsKey(id);
	}
	
	private static void push(SymbolTable s) {
		
	}
	
	public static void push() {
		
	}
}
