package com.ufcg.compiladores;

public class ErrorCounter {
	
	private static int value = 0;
	private static int limit = 10;
	
	private ErrorCounter() {}
	
	public static void increase() {
		if(value < limit) value += 1;
		else {
			System.err.println("Too many errors, aborting execution");
			System.exit(1);
		}
	}
	
	public static int get() {
		return value;
	}
}
