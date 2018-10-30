package com.ufcg.compiladores;

import java_cup.runtime.ComplexSymbolFactory.*;

public class Token {
	
	public String name;
	public Location loc;
	
	public Token(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}
}
