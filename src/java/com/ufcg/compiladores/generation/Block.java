package com.ufcg.compiladores.generation;

import java.util.List;

import com.ufcg.compiladores.*;

public class Block implements Generator.Visitable {
	public List<Decl> d;
	public List<Stmt> s;
	
	public Block(List<Decl> d, List<Stmt> s) {
		this.d = d;
		this.s = s;
	}
	
	public void accept(Generator gen) {
		if(gen.preVisit(this) == false) return;
		for(Decl decl : d) decl.accept(gen);
		for(Stmt stmt : s) stmt.accept(gen);
		gen.postVisit(this);
	}
}
