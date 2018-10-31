package com.ufcg.compiladores.generation;

import java.util.*;

import com.ufcg.compiladores.*;

public abstract class Expr implements Generator.Visitable {

	public Type t;
	public String ref;
	public Boolean dynamic;

    public static class Relop extends Expr {

    	public Expr e1, e2;
    	public String op;
    	
    	public String l1 = Ref.lab.get();
    	public String l2 = Ref.lab.get();

		public Relop(Expr e1, String op, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			this.op = op;

			switch (Type.Integer.check(e1.t, e2.t))
			{
			case OK:
				this.t = Type.Boolean;
				break;

			case ERROR:
				Log.error(String.format("Operator not defined for types %s and %s", e1.t, e2.t));
				break;

			case IGNORE:
				break;
			}

			this.dynamic = e1.dynamic || e2.dynamic;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e1.accept(gen);
			e2.accept(gen);
			gen.postVisit(this);
		}
    }

    public static class Binop extends Expr {
    	public Expr e1, e2;
    	public String op;

		public Binop(Expr e1, String op, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			this.op = op;

			switch (Type.Integer.check(e1.t, e2.t))
			{
			case OK:
				this.t = Type.Integer;
				break;

			case ERROR:
				Log.error(String.format("Operator not defined for types %s and %s", e1.t, e2.t));
				break;

			case IGNORE:
				break;
			}

			this.dynamic = e1.dynamic || e2.dynamic;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e1.accept(gen);
			e2.accept(gen);
			gen.postVisit(this);
		}
    }

    public static class Unop extends Expr {
    	public Expr e;
    	public String op;

		public Unop(String op, Expr e) {
			this.e = e;
			this.op = op;

			switch (Type.Integer.check(e.t))
			{
			case OK:
				this.t = Type.Integer;
				break;

			case ERROR:
				Log.error(String.format("Operator not defined for type %s", e.t));
				break;

			case IGNORE:
				break;
			}
			
			this.dynamic = e.dynamic;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e.accept(gen);
			gen.postVisit(this);
		}
    }

    public static class Identifier extends Expr {
    	public Scope.Instance i;

		public Identifier(Scope.Instance i) {
			if (i != null) {
				this.i = i;
				this.t = i.t;
			}

			this.dynamic = true;
		}

		@Override
		public void accept(Generator gen) {
			gen.visit(this);
		}
	}

    public static class Literal extends Expr {
    	public Object val;

		public Literal(Type t, Object val) {
			this.t = t;
			this.val = val;

			this.dynamic = false;
		}

		@Override
		public void accept(Generator gen) {
			gen.visit(this);
		}
	}

    public static class Call extends Expr {
    	
    	public String l;
    	public List<Expr> args;

		public Call(Scope.Instance i, List<Expr> args) {
			if (i == null);
			
			else if (i.t instanceof Type.Call) {

				this.t = ((Type.Call) i.t).ret;
				
				this.l = i.ref;
				this.args = args;
				
				List<Type> types = new ArrayList<>();
				for(Expr e : args) types.add(e.t);
				
				Type t = new Type.Call(types, null);
				
				switch (i.t.check(t)) {
				case ERROR:
					Log.error(String.format("Arguments do not match %s", i.t));
					break;
					
				default:
					break;
				}
			}
			
			else {
				Log.error(String.format("Type %s is not callable", i.t));
			}

			this.dynamic = true;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			for(Expr e : args) e.accept(gen);
			gen.postVisit(this);
		}
	}
    
    public static class Array extends Expr {
    	
    	public String ptr;
    	public Scope.Instance i;
    	public List<Expr> e;
    	
    	public List<Type.Range> s = new ArrayList<>();
    	
    	public Array(Scope.Instance i, List<Expr> e) {
    		this.i = i;
    		this.e = e;
    		
    		if (i == null);
    		
    		else if (i.t instanceof Type.Array) {
    			
    			Type cur = i.t;
    			while (cur instanceof Type.Array) {
    				Type.Array t = (Type.Array) cur;
    				
    				s.add(t.s);
    				cur = t.t;
    			}
    			
    			this.t = cur;
    			
    			if (e.size() != s.size()) {
    				Log.error(String.format("Number of indexes should be %d", s.size()));
    			}
    		}
    		
    		else {
    			Log.error(String.format("Type %s is not indexable", i.t));
    		}
    		
			this.dynamic = true;
    	}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			for(Expr expr : e) expr.accept(gen);
			gen.postVisit(this);
		}
    }
}
