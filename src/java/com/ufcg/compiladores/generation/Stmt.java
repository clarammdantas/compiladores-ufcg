package com.ufcg.compiladores.generation;

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
}
