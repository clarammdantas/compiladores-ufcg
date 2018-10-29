package com.ufcg.compiladores;

import java.io.IOException;
import java.io.PrintWriter;

import com.ufcg.compiladores.generation.*;

public class Generator {
	PrintWriter writer;

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

	public boolean preVisit(Program program) {
		writer.println("section .text");
		writer.println("\tglobal _start");
		writer.println("_start:");
		return true;
	}

	public void postVisit(Program program) {
		writer.println("_end:");
		writer.println("\tmov eax, 1");
		writer.println("\tmov ebx, 0");
		writer.println("\tint 0x80\n");

		writer.println("section .bss");
		for (int i = 0; i < Temp.size(); ++i) {
			writer.println("\tt" + i + " resd 1");
		}
	}

	public boolean preVisit(Stmt.Assign s) {
		// TODO Auto-generated method stub
		return true;
	}

	public void postVisit(Stmt.Assign s) {
		Temp.release();
	}

	public boolean preVisit(Stmt.Repeat r) {
		r.label = Label.get();
		writer.println(String.format("%s:", r.label));
		return true;
	}

	public void postVisit(Stmt.Repeat r) {
		Temp.release();

		writer.println(String.format("\tmov al, [%s]", r.e.reg));
		writer.println(String.format("\tcmp al, 1"));
		writer.println(String.format("\tjl %s\n", r.label));
	}

	public boolean preVisit(Expr.Binex e) {
		return true;
	}

	public void postVisit(Expr.Binex e) {
		Temp.release(2);
		e.reg = Temp.get();

		writer.println(String.format("\tmov eax, [%s]", e.e1.reg));
		writer.println(String.format("\tmov ebx, [%s]", e.e2.reg));

		switch (e.op) {
		case "add":
			writer.println(String.format("\tadd eax, ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.reg));
			break;

		case "sub":
			writer.println(String.format("\tsub eax, ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.reg));
			break;

		case "mul":
			writer.println(String.format("\timul ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.reg));
			break;

		case "div":
			writer.println(String.format("\tmov edx, 0", e.reg));
			writer.println(String.format("\tidiv ebx"));
			writer.println(String.format("\tmov [%s], eax\n", e.reg));
			break;

		case "mod":
			writer.println(String.format("\tmov edx, 0", e.reg));
			writer.println(String.format("\tidiv ebx"));
			writer.println(String.format("\tmov [%s], edx\n", e.reg));
			break;
		}
	}

	public boolean preVisit(Expr.Unex e) {
		return true;
	}

	public void postVisit(Expr.Unex e) {
		Temp.release();
		e.reg = Temp.get();

		writer.println(String.format("\tmov eax, [%s]", e.e.reg));
		writer.println(String.format("\t%s eax", e.op));
		writer.println(String.format("\tmov [%s], eax\n", e.reg));
	}

	public boolean preVisit(Expr.Literal e) {
		return true;
	}

	public void postVisit(Expr.Literal e) {
		e.reg = Temp.get();

		writer.println(String.format("\tmov eax, %d", e.val));
		writer.println(String.format("\tmov [%s], eax\n", e.reg));
	}
}
