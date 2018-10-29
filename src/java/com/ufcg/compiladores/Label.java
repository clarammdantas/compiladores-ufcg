package com.ufcg.compiladores;

public class Label {
	private static int count = 0;
	
	private Label() {}
	
	public static String get() {
		String label = "l" + count;
		count += 1;
		return label;
	}
}
