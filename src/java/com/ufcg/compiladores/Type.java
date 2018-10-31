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
