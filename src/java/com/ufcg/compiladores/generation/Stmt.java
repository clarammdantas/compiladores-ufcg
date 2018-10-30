package com.ufcg.compiladores.generation;

import java.util.*;

import com.ufcg.compiladores.*;

public abstract class Stmt implements Generator.Visitable {
	
	public static class Assign extends Stmt {
		public Scope.Instance i;
		public Expr e;
		
		public Assign(Scope.Instance i, Expr e) {
			this.i = i;
			this.e = e;
			
			if(i != null) {
				switch (i.t.check(e.t)) {
				case ERROR:
					String message = String.format("Incompatible types \"%s\" and \"%s\"", i.t, e.t);
					Log.error(message);
					break;
					
				default:
					break;
				}
			}
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e.accept(gen);
			gen.postVisit(this);
		}
	}
	
	public static class Call extends Stmt {
		public Expr c;
		
		public Call(Expr c) {
			this.c = c;
		}

		@Override
		public void accept(Generator gen) {
			c.accept(gen);
		}
	}
	
	public static class Repeat extends Stmt {
		
		public List<Stmt> s;
		public Expr e;
		
		public String l = Ref.lab.get();
		
		public Repeat(List<Stmt> s, Expr e) {
			this.s = s;
			this.e = e;

			switch (Type.Boolean.check(e.t))
			{
			case ERROR:
				Log.error("Expected a Boolean expression");
				break;

			default:
				break;
			}
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			for(Stmt stmt : s) stmt.accept(gen);
			e.accept(gen);
			gen.postVisit(this);
		}
	}
}
