package com.ufcg.compiladores;

import java.io.FileReader;

public class Main {
	public static Lexer pascalLexer;
	public static Parser p; 
	public static void main(String[] args) {
        try {
        	pascalLexer = new Lexer(new FileReader("src/pascal/program.pas"));
            p = new Parser(pascalLexer);
            Object result = p.parse().value;

            if (p.numErrors > 0) {
            	System.err.println("Compilation error!");
            } else {
            	System.out.println("Compilation successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
