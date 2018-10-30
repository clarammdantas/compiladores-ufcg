package com.ufcg.compiladores.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcedureType extends LiteralType {
	
	private List<LiteralType> paramsTypes;
	private final static String PROC_TYPE_NAME = "procedure_type"; 
	
	public ProcedureType() {
		super(PROC_TYPE_NAME);
		this.paramsTypes = new ArrayList<>();
	}
	
	public List<LiteralType> getParamsTypes() {
		return this.paramsTypes;
	}
	
	public void addParam(LiteralType param) {
		this.paramsTypes.add(param);
	}
	
	public void addParams(List<LiteralType> type) {
		this.paramsTypes.addAll(type);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.paramsTypes.toArray());
	}
}
