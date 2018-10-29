package com.ufcg.compiladores.generation;

import java.util.List;

import com.ufcg.compiladores.Generator;

public abstract class Stmt {

	protected abstract void accept(Generator gen);
	
	public static Assign assign(String id, Expr e) {
		return new Assign(id, e);
	}
	
	public static class Assign extends Stmt {
		public String id;
		public Expr e;
		
		public Assign(String id, Expr e) {
			this.id = id;
			this.e = e;
		}

		@Override
		protected void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e.accept(gen);
			gen.postVisit(this);
		}
	}
	
	public static Repeat repeat(List<Stmt> s, Expr e) {
		return new Repeat(s, e);
	}
	
	public static class Repeat extends Stmt {
		public String label;
		
		public List<Stmt> s;
		public Expr e;
		
		public Repeat(List<Stmt> s, Expr e) {
			this.s = s;
			this.e = e;
		}

		@Override
		protected void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			for(int i=0; i < s.size(); ++i) s.get(i).accept(gen);
			e.accept(gen);
			gen.postVisit(this);
		}
	}
}
