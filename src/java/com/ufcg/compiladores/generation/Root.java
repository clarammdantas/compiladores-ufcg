package com.ufcg.compiladores.generation;

import com.ufcg.compiladores.*;

public class Root implements Generator.Visitable {
	Block b;
	
	public Root(Block b) {
		this.b = b;
	}
	
	public void accept(Generator gen) {
		if(gen.preVisit(this) == false) return;
		b.accept(gen);
		gen.postVisit(this);
	}
}
