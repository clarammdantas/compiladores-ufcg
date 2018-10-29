package com.ufcg.compiladores.generation;

import java.util.List;

public class Program {
	
	List<Decl> decl;
	List<Stmt> stmt;
	
	public Program(List<Decl> decl, List<Stmt> stmt) {
		this.decl = decl;
		this.stmt = stmt;
	}
	
	public void accept(Generator gen) {
		if(gen.preVisit(this) == false) return;
//		for(int i=0; i < decl.size(); ++i) decl.get(i).accept(gen);
		for(int i=0; i < stmt.size(); ++i) stmt.get(i).accept(gen);
		gen.postVisit(this);
	}
}
