package com.ufcg.compiladores;

import java.io.FileReader;

public class Main {
	public static void main(String[] args) {
        try {
            Parser p = new Parser(new Lexer(new FileReader("src/main/pascal/program.pas")));
            Object result = p.parse().value;

            System.out.println("Compilation successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
