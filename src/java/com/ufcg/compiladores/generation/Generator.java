package com.ufcg.compiladores.generation;

import java.io.IOException;
import java.io.PrintWriter;

import com.ufcg.compiladores.generation.Expr.Binex;
import com.ufcg.compiladores.generation.Expr.IntegerLiteral;
import com.ufcg.compiladores.generation.Expr.Unex;
import com.ufcg.compiladores.generation.Stmt.Assign;

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
		for (int i = 0; i < Temp.getSize(); ++i) {
			writer.println("\tt" + i + " resd 1");
		}
	}

	public boolean preVisit(Assign s) {
		return true;
	}

	public void postVisit(Assign s) {
	}

	public boolean preVisit(Binex e) {
		return true;
	}

	public void postVisit(Binex e) {
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

	public boolean preVisit(Unex e) {
		return true;
	}

	public void postVisit(Unex e) {
		Temp.release();
		e.reg = Temp.get();

		writer.println(String.format("\tmov eax, [%s]", e.e.reg));
		writer.println(String.format("\t%s eax", e.op));
		writer.println(String.format("\tmov [%s], eax\n", e.reg));
	}

	public boolean preVisit(Expr.IntegerLiteral e) {
		return true;
	}

	public void postVisit(Expr.IntegerLiteral e) {
		e.reg = Temp.get();

		writer.println(String.format("\tmov eax, %d", e.val));
		writer.println(String.format("\tmov [%s], eax\n", e.reg));
	}
}
