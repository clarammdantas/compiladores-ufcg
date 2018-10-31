package com.ufcg.compiladores;

public class Ref {
	
	public static Ref tmp = new Ref("t"); 
	public static Ref var = new Ref("v");
	public static Ref arr = new Ref("a");
	public static Ref lab = new Ref("l");
	
	private String prefix;
	
	private int count = 0;
	private int max = 0;
	private int saved = 0;
	
	private Ref(String prefix) {
		this.prefix = prefix;
	}
	
	public String get() {
		String temp = prefix + count;
		count += 1;
		max = Math.max(count, max);
		return temp;
	}
	
	public void save() {
		saved = count;
	}
	
	public void restore() {
		count = saved;
	}
	
	public int now() {
		return count;
	}
	
	public int size() {
		return max;
	}
}
