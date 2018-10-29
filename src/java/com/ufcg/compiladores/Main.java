package com.ufcg.compiladores;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
	
	static Lexer lexer;
	static ErrorCounter errors;
	
	public static void main(String[] args) throws FileNotFoundException {
		errors = new ErrorCounter();
    	
    	lexer = new Lexer(new FileReader("src/pascal/test.pas"), errors);
        Parser p = new Parser(lexer);
        
        try {
            Object result = p.parse().value;

            if(errors.count() == 0) System.out.println("Compilation successful");
            else {
            	String output = String.format("There were %d errors compiling module", errors.count());
            	System.err.println(output);
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
	}
}
