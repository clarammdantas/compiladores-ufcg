package com.ufcg.compiladores;

import java.io.*;

import com.ufcg.compiladores.generation.*;

import java_cup.runtime.*;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
	    ComplexSymbolFactory csf = new ComplexSymbolFactory();
	    BufferedReader reader = new BufferedReader(new FileReader("src/pascal/program.pas"));
	    
	    ScannerBuffer lexer = new ScannerBuffer(new Lexer(reader, csf));
		Parser p = new Parser(lexer, csf);
		
		try {
			Root result = (Root) p.parse().value;

			if(Log.size() == 0) {
				Generator gen = new Generator("target/asm/program.asm");
				result.accept(gen);
				gen.flush();
			}
			
			else {
				String output = String.format("There were %d errors compiling module", Log.size());
				System.err.println(output);
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
