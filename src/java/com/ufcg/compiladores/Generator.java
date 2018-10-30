package com.ufcg.compiladores;

import java.io.IOException;
import java.io.PrintWriter;

import com.ufcg.compiladores.generation.*;

public class Generator {
	PrintWriter writer;
	
	public static interface Visitable {
		public void accept(Generator gen);
	}

	public Generator(String filename) {
		try {
			writer = new PrintWriter(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flush() {
		writer.flush();
		writer.close();
	}

	public boolean preVisit(Root p) {
		writer.println("section .text");
		writer.println("\tglobal _start\n");
		writer.println("_start:");
		return true;
	}

	public void postVisit(Root p) {
		writer.println("_end:");
		writer.println("\tmov eax, 1");
		writer.println("\tmov ebx, 0");
		writer.println("\tint 0x80\n");

		writer.println("section .bss");
		
		for (int i = 0; i < Ref.tmp.size(); ++i) writer.println("\tt" + i + " resd 1");
		for (int i = 0; i < Ref.var.size(); ++i) writer.println("\tv" + i + " resd 1");
	}

	public boolean preVisit(Block b) {
		return true;
	}

	public void postVisit(Block b) {
	}

	public boolean preVisit(Decl.Procedure p) {
		writer.println(String.format("\tjmp %s", p.l2));
		writer.println(String.format("%s:", p.l1));
		
		for (int i = p.varLo; i < p.varHi; ++i) {
			writer.println(String.format("\tmov eax, [v%d]", i));
			writer.println(String.format("\tpush eax\n"));
		}
		
		for (int i = 0; i < p.varPm; ++i) {
			writer.println(String.format("\tmov eax, [t%d]", i));
			writer.println(String.format("\tmov [v%d], eax\n", i + p.varLo));
		}
		
		return true;
	}

	public void postVisit(Decl.Procedure p) {
		for (int i = p.varHi - 1; i >= p.varLo; --i) {
			writer.println(String.format("\tpop eax"));
			writer.println(String.format("\tmov [v%d], eax", i));
		}
		
		writer.println("\tret");
		writer.println(String.format("%s:", p.l2));
	}

	public boolean preVisit(Stmt.Assign s) {
		Ref.tmp.save();
		return true;
	}

	public void postVisit(Stmt.Assign s) {
		Ref.tmp.restore();

		writer.println(String.format("\tmov eax, [%s]", s.e.ref));
		writer.println(String.format("\tmov [%s], eax\n", s.i.ref));
	}

	public boolean preVisit(Stmt.Repeat r) {
		writer.println(String.format("%s:", r.l));
		
		Ref.tmp.save();
		return true;
	}

	public void postVisit(Stmt.Repeat r) {
		Ref.tmp.restore();

		writer.println(String.format("\tmov al, [%s]", r.e.ref));
		writer.println(String.format("\tcmp al, 0"));
		writer.println(String.format("\tje %s\n", r.l));
	}

	public boolean preVisit(Expr.Relop e) {
		Ref.tmp.save();
		return true;
	}

	public void postVisit(Expr.Relop e) {
		Ref.tmp.restore();
		
		e.ref = Ref.tmp.get();
		
		writer.println(String.format("\tmov eax, [%s]", e.e1.ref));
		writer.println(String.format("\tmov ebx, [%s]", e.e2.ref));
		
		writer.println(String.format("\tcmp eax, ebx"));
		writer.println(String.format("\t%s %s", e.op, e.l1));

		writer.println(String.format("\tmov eax, 0"));
		writer.println(String.format("\tmov [%s], eax", e.ref));
		writer.println(String.format("\tjmp %s", e.l2));
		
		writer.println(String.format("%s:", e.l1));
		writer.println(String.format("\tmov eax, 1"));
		writer.println(String.format("\tmov [%s], eax", e.ref));
		
		writer.println(String.format("%s:", e.l2));
	}

	public boolean preVisit(Expr.Binop e) {
		Ref.tmp.save();
		return true;
	}

	public void postVisit(Expr.Binop e) {
		Ref.tmp.restore();
		
		e.ref = Ref.tmp.get();

		writer.println(String.format("\tmov eax, [%s]", e.e1.ref));
		writer.println(String.format("\tmov ebx, [%s]", e.e2.ref));

		switch (e.op) {
		case "add":
			writer.println(String.format("\tadd eax, ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.ref));
			break;

		case "sub":
			writer.println(String.format("\tsub eax, ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.ref));
			break;

		case "mul":
			writer.println(String.format("\timul ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.ref));
			break;

		case "div":
			writer.println(String.format("\tmov edx, 0", e.ref));
			writer.println(String.format("\tidiv ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.ref));
			break;

		case "mod":
			writer.println(String.format("\tmov edx, 0", e.ref));
			writer.println(String.format("\tidiv ebx"));
			writer.println(String.format("\tmov [%s], edx\n", e.ref));
			break;
		}
	}

	public boolean preVisit(Expr.Unop e) {
		Ref.tmp.save();
		return true;
	}

	public void postVisit(Expr.Unop e) {
		Ref.tmp.restore();
		
		e.ref = Ref.tmp.get();

		writer.println(String.format("\tmov eax, [%s]", e.e.ref));
		writer.println(String.format("\t%s eax", e.op));
		writer.println(String.format("\tmov [%s], eax\n", e.ref));
	}

	public void visit(Expr.Identifier i) {
		i.ref = i.i.ref;
	}

	public boolean preVisit(Expr.Call c) {
		Ref.tmp.save();
		return true;
	}

	public void postVisit(Expr.Call c) {
		Ref.tmp.restore();
		writer.println(String.format("\tcall %s\n", c.l));
	}

	public void visit(Expr.Literal e) {
		e.ref = Ref.tmp.get();

		writer.println(String.format("\tmov eax, %d", e.val));
		writer.println(String.format("\tmov [%s], eax\n", e.ref));
	}
}
