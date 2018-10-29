package com.ufcg.compiladores;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.ufcg.compiladores.generation.Generator;
import com.ufcg.compiladores.generation.Program;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		
		Lexer lexer = new Lexer(new FileReader("src/pascal/program.pas"));
		Parser p = new Parser(lexer);
		
		try {
			Program result = (Program) p.parse().value;

			if(ErrorCounter.get() == 0) {
				Generator gen = new Generator("target/asm/program.asm");
				result.accept(gen);
				gen.flush();
			}
			
			else {
				String output = String.format("There were %d errors compiling module", ErrorCounter.get());
				System.err.println(output);
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
