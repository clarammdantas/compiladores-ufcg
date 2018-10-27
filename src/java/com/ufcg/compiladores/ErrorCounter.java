package com.ufcg.compiladores;

public class ErrorCounter {
	
	private int value = 0;
	private int limit;
	
	ErrorCounter() {
		this(10);
	}
	
	ErrorCounter(int limit) {
		this.limit = limit;
	}
	
	public void increase() {
		if(value < limit) value += 1;
		else {
			System.err.println("Too many errors, aborting execution");
			System.exit(1);
		}
	}
	
	public int count() {
		return value;
	}
}
