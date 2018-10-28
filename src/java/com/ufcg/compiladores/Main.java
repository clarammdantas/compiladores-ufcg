package com.ufcg.compiladores;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		Lexer lexer = new Lexer(new FileReader("src/pascal/program.pas"));
		Parser p = new Parser(lexer);
		
		try {
			Object result = p.parse().value;

			if(ErrorCounter.get() > 0) {
				String output = String.format("There were %d errors compiling module", ErrorCounter.get());
				System.err.println(output);
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
