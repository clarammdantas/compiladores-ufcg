package com.ufcg.compiladores.generation;

import java.util.*;

import com.ufcg.compiladores.*;

public abstract class Decl implements Generator.Visitable {
	
	public static Decl noop = new Decl() {
		@Override
		public void accept(Generator gen) {}
	};
	
	public static class Procedure extends Decl {
		
		public int varLo;
		public int varHi;
		public int varPm;
		
		public String l1, l2;
		public Block b;
		
		public Procedure(String id, List<Type> p, Block b) {
			this.b = b;
			
			this.varHi = Ref.var.now();
			Ref.var.restore();
			this.varLo = Ref.var.now();
			this.varPm = p.size();
			
			this.l1 = Scope.get(id).ref;
			this.l2 = Ref.lab.get();
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			b.accept(gen);
			gen.postVisit(this);
		}
	}
}
