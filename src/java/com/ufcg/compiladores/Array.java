package com.ufcg.compiladores;

import java.util.*;

public class Array {
	
	public static List<Array> list = new ArrayList<>();
	
	public String ref;
	public Integer size;
	
	public Array(String ref, Type t) {
		this.ref = ref;		
		this.size = 1;
		
		Type cur = t;
		while (cur instanceof Type.Array) {
			Type.Array a = (Type.Array) cur;
			cur = a.t;
			
			this.size = this.size * (a.s.r - a.s.l + 1);
		}
	}
	
	public static String get(Type t) {
		Array a = new Array(Ref.arr.get(), t);
		
		list.add(a);
		return a.ref;
	}
}
