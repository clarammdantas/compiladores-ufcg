package com.ufcg.compiladores.generation;

public abstract class Expr {
	
	public String reg;
	
    public abstract void accept(Generator gen);
    
    public static Binex binop(Expr e1, String op, Expr e2) {
    	return new Binex(e1, op, e2);
    }
    
    public static class Binex extends Expr {
    	public Expr e1, e2;
    	public String op;
    	
		public Binex(Expr e1, String op, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			this.op = op;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e1.accept(gen);
			e2.accept(gen);
			gen.postVisit(this);
		}
    }
    
    public static Unex unop(String op, Expr e) {
    	return new Unex(op, e);
    }
    
    public static class Unex extends Expr {
    	public Expr e;
    	public String op;
    	
		public Unex(String op, Expr e) {
			this.e = e;
			this.op = op;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			e.accept(gen);
			gen.postVisit(this);
		}
    }
    
    public static IntegerLiteral integer(Integer val) {
    	return new IntegerLiteral(val);
    }
    
    public static class IntegerLiteral extends Expr {
    	public Integer val;
    	
		public IntegerLiteral(Integer val) {
			this.val = val;
		}

		@Override
		public void accept(Generator gen) {
			if(gen.preVisit(this) == false) return;
			gen.postVisit(this);
		}
    }
}
