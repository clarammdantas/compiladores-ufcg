package com.ufcg.compiladores;

public class Temp {
	private static int count = 0;
	private static int size = 0;
	
	private Temp() {}
	
	public static String get() {
		String temp = "t" + count;
		count += 1;
		size = Math.max(count, size);
		return temp;
	}
	
	public static void release() {
		release(1);
	}
	
	public static void release(int n) {
		count -= n;
	}
	
	public static int size() {
		return size;
	}
}
