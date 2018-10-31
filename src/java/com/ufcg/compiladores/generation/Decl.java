package com.ufcg.compiladores.generation;

import java.util.*;

import com.ufcg.compiladores.*;

public abstract class Decl implements Generator.Visitable {
	
	public static Decl noop = new Decl() {
		@Override
		public void accept(Generator gen) {}
	};
	
	public static class Compose extends Decl {
		
		public List<Decl> d = new ArrayList<>();
		
		public Compose(List<Decl> d) {
			this.d = d;
		}

		@Override
		public void accept(Generator gen) {
			for(Decl decl : d) decl.accept(gen);
		}
	}
	
	public static class Init extends Decl {
		
		public Stmt.Assign s;
		
		public Init(Scope.Instance i, Expr e) {
			if (e != null) this.s = new Stmt.Assign(i, e);
		}

		@Override
		public void accept(Generator gen) {
			if (s != null) s.accept(gen);
		}
	}
	
	private static abstract class Callable extends Decl {
		
		public int varLo;
		public int varHi;
		public int varPm;
		
		public String l1, l2;
		public Block b;
		
		public Callable(String id, List<Type> p, Block b) {
			this.b = b;
			
			this.varHi = Ref.var.now();
			Ref.var.restore();
			this.varLo = Ref.var.now();
			this.varPm = p.size();
			
			this.l1 = Scope.get('.' + id).ref;
			this.l2 = Ref.lab.get();
		}
	}
	
	public static class Procedure extends Callable {

		public Procedure(String id, List<Type> p, Block b) {
			super(id, p, b);
		}

		@Override
		public void accept(Generator gen) {
			if (gen.preVisit(this) == false) return;
			b.accept(gen);
			gen.postVisit(this);
		}
	}
	
	public static class Function extends Callable {
	
		public String ret;
		
		public Function(String id, List<Type> p, Block b) {
			super(id, p, b);			
			
			this.ret = Scope.get(id).ref;
		}

		@Override
		public void accept(Generator gen) {
			if (gen.preVisit(this) == false) return;
			b.accept(gen);
			gen.postVisit(this);
		}
	}
}
