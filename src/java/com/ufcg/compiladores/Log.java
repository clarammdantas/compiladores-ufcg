package com.ufcg.compiladores;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class Log {
	
	private static int size = 0;

	private static int line;
	private static int column;
	
	private Log() {}
	
	public static int size() {
		return size;
	}
	
	public static void focus(int line, int column) {
		Log.line = line;
		Log.column = column;
	}
	
	public static void focus(Location loc) {
		Log.line = loc.getLine();
		Log.column = loc.getColumn();
	}
	
	public static void error(String message) {
		report("Error", message);
	}
	
	public static void fatal(String message) {
		report("Fatal", message);
		System.exit(1);
	}
	
	private static void report(String level, String message) {		
		size += 1;
		String output = String.format("(%d,%d) %s: %s", line+1, column+1, level, message);
		System.err.println(output);
	}
}
