package com.ufcg.compiladores.type;

import java.util.ArrayList;
import java.util.List;

public class FunctionType extends LiteralType{
	
	private LiteralType returnType;
	private List<LiteralType> paramsTypes;
	
	public FunctionType(LiteralType returnType) {
		super(returnType.getName());
		this.returnType = returnType;
		this.paramsTypes = new ArrayList<>();
	}
	
	public LiteralType getReturnType() {
		return this.returnType;
	}
	
	public List<LiteralType> getParamsTypes() {
		return this.paramsTypes;
	}
	
	public void addParam(LiteralType param) {
		this.paramsTypes.add(param);
	}
}
