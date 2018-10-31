package com.ufcg.compiladores;

import java.util.*;

public abstract class Type {
	
	public enum Validation {
		OK,
		ERROR,
		IGNORE
	}
	
	public Validation check(Type ...types) {
		for(Type t : types) {
			if(t == null) return Validation.IGNORE;
			if(equals(t) == false) return Validation.ERROR;
		}
		
		return Validation.OK;
	}

	public static Type translate(String name) {
		switch (name) {
		case "integer":
			return Integer;
			
		case "boolean":
			return Boolean;
			
		case "string":
			return Text;
		
		default:
			Log.error("Type not recognized");
			return null;
		}
	}
	
	public static Type Integer = new Type() {
		@Override
		public String toString() {
			return "Integer";
		}
	};
	
	public static Type Boolean = new Type() {
		@Override
		public String toString() {
			return "Boolean";
		}
	};
	
	public static Type Text = new Type() {
		@Override
		public String toString() {
			return "String";
		}
	};
	
	public static Type Void = new Type() {
		@Override
		public String toString() {
			return "Void";
		}
	};
	
	public static class Range extends Type {
		
		public Integer l;
		public Integer r;
		
		public Range(int l, int r) {
			this.l = l;
			this.r = r;
		}
		
		@Override
		public String toString() {	
			StringBuilder builder = new StringBuilder();
			
			builder.append("[");		
			builder.append(l.toString());
			builder.append("..");
			builder.append(r.toString());
			builder.append("]");
			
			return builder.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Range == false) return false;
			
			Range other = (Range) obj;
			
			return l.equals(other.l) && r.equals(other.r);
		}
	}
	
	public static class Array extends Type {
		
		public Type.Range s;
		public Type t;
		
		public Array(Type.Range s, Type t) {
			this.s = s;
			this.t = t;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			
			builder.append("Array");
			builder.append(s.toString());
			builder.append(" of ");
			builder.append(t.toString());
			
			return builder.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Array == false) return false;
			
			Array other = (Array) obj;
			
			return s.equals(other.s) && t.equals(other.t);
		}
	}
	
	public static class Call extends Type {
		
		public List<Type> params;
		public Type ret;
		
		public Call(List<Type> params, Type ret) {
			this.params = params;
			this.ret = ret;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();

			List<String> types = new ArrayList<>();
			for(Type t : params) types.add(t.toString());	
			
			builder.append("<");		
			builder.append(String.join(", ", types));
			builder.append(">");
			
			return builder.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Call == false) return false;
			
			Call other = (Call) obj;
			
			if(params.size() != other.params.size()) return false;
			for(int i = 0; i < params.size(); ++i) {
				if(params.get(i).check(other.params.get(i)) == Validation.ERROR) return false;
			}
			
			return true;
		}
		
	}
}
