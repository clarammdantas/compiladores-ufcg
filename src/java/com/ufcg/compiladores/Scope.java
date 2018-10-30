package com.ufcg.compiladores;

import java.util.*;

public class Scope {
	

	public static class Instance {
		public Type t;
		public String ref;
		
		public Instance(Type t, String ref) {
			this.t = t;
			this.ref = ref;
		}
	}
	
	private static LinkedList<Map<String, Instance>> stack = new LinkedList<>();
	
	public static void push() {
		Map<String, Instance> scope = new HashMap<>();
		stack.push(scope);
	}
	
	public static void pop() {
		stack.pop();
	}
	
	public static Instance get(String name) {
		for (Map<String, Instance> scope: stack) {
			
			Instance result = scope.get(name);
			if(result != null) return result;
		}
		
		String message = String.format("Symbol \"%s\" not declared", name);
		Log.error(message);
		
		return null;
	}
	
	public static void add(String name, Type t, Ref ref) {
		if(contains(name)) {
			String message = String.format("Symbol \"%s\" already declared", name);
			Log.error(message);
			
			return;
		}
		
		overwrite(name, t, ref);
	}
	
	public static void overwrite(String name, Type t, Ref ref) {
		Map<String, Instance> scope = stack.peek();
		Instance i = new Instance(t, ref.get());
		scope.put(name, i);
	}
	
	private static boolean contains(String name) {
		for (Map<String, Instance> scope: stack) {
			
			Instance result = scope.get(name);
			if(result != null) return true;
		}
		
		return false;
	}
}
