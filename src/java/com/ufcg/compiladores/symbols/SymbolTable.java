package com.ufcg.compiladores.symbols;

import java.util.HashMap;

import com.ufcg.compiladores.type.LiteralType;

public class SymbolTable {
	
	public static SymbolTable currentScope;
	public static SymbolTable root = new SymbolTable(null);
	
	HashMap<String, LiteralType> table;
	private SymbolTable previousScope;
	
	public SymbolTable(SymbolTable previousScope) {
		this.table = new HashMap<>();
		this.previousScope = previousScope;
	}
	
	public static void start() {
		SymbolTable.currentScope = root;
	}
	
	public static boolean addSymbol(String id, LiteralType type) {
		if (SymbolTable.currentScope.table.get(id) != null) 
			return false;
		else {
			SymbolTable.currentScope.table.put(id, type);
			return true;
		}
	}
	
	public static LiteralType getSymbol(String id) {
		return SymbolTable.currentScope.table.get(id);
	}
	
	private static void push(SymbolTable s) {
		SymbolTable.currentScope = new SymbolTable(s);
	}
	
	public static void push() {
		push(currentScope);
	}
	
	public static void pop() {
		if (currentScope.previousScope != null)
			currentScope = currentScope.previousScope;
	}
}
