package com.ufcg.compiladores;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.*;

%%

%class Lexer
%unicode
%ignorecase
%cup
%line
%column

%{
    ComplexSymbolFactory symbolFactory;
	StringBuilder string = new StringBuilder();
	
	public Lexer(java.io.Reader in, ComplexSymbolFactory sf) {
		this(in);
		symbolFactory = sf;
    }
    
    private Symbol symbol(int type) {
		return symbol(type, null);
	}

	private Symbol symbol(int type, Object value) {
		String name = sym.terminalNames[type];
		
		Location left = new Location(yyline, yycolumn, yychar);
		Location right = new Location(yyline, yycolumn + yylength(), yychar + yylength());
		
		return symbolFactory.newSymbol(name, type, left, right, value);
	}
	
	private void error(String message) {
		Log.focus(yyline, yycolumn);
		Log.error(message);
	}
	
	private void fatal(String message) {
		Log.focus(yyline, yycolumn);
		Log.fatal(message);
	}
	
	private void parseEscape() {
		int code = Integer.parseInt(yytext());
		if(code > 255) error("Illegal char constant");
		string.append((char) code);
	}
%}

%eofval{
	Location left = new Location(yyline, yycolumn, yychar);
	Location right = new Location(yyline, yycolumn, yychar + 1);
	
	return symbolFactory.newSymbol("EOF", sym.EOF, left, right);
%eofval}


Whitespace		= [ \r\n\t]
LineTerminator	= \r|\n|\r\n

Integer			= [0-9]+
String			= [^\r\n\']+

Identifier		= [_a-zA-Z][_a-zA-Z0-9]*
Invalid			= [0-9]+[_a-zA-Z][_a-zA-Z0-9]*

%state STRING, ESCAPE, BRANCH

%%

<YYINITIAL> {
	"program"				{ return symbol(sym.PROGRAM); }
	"begin"					{ return symbol(sym.BEGIN); }
	"end"					{ return symbol(sym.END); }
	"var"					{ return symbol(sym.VAR); }
	"function"				{ return symbol(sym.FUNCTION); }
	"procedure"				{ return symbol(sym.PROCEDURE); }
	"repeat"				{ return symbol(sym.REPEAT); }
	"until"					{ return symbol(sym.UNTIL); }
	
	"array"					{ return symbol(sym.ARRAY); }
	"of"					{ return symbol(sym.OF); }
	".."					{ return symbol(sym.RANGE); }
	
	"("						{ return symbol(sym.LEFT_PARENTHESIS); }
	")"						{ return symbol(sym.RIGHT_PARENTHESIS); }
	"["						{ return symbol(sym.LEFT_BRACKET); }
	"]"						{ return symbol(sym.RIGHT_BRACKET); }
	
	","						{ return symbol(sym.COMMA); }
	":"						{ return symbol(sym.COLON); }
	";"						{ return symbol(sym.SEMICOLON); }
	"."						{ return symbol(sym.DOT); }
	
	"="						{ return symbol(sym.EQ); }
	"<>"					{ return symbol(sym.NE); }
	">"						{ return symbol(sym.GT); }
	"<"						{ return symbol(sym.LT); }
	">="					{ return symbol(sym.GE); }
	"<="					{ return symbol(sym.LE); }
	
	"+"						{ return symbol(sym.ADD); }
	"-"						{ return symbol(sym.SUB); }
	"*"						{ return symbol(sym.MUL); }
	"/" | "div"				{ return symbol(sym.DIV); }
	"mod"					{ return symbol(sym.MOD); }
	
	":="					{ return symbol(sym.ASSIGN); }
	
	"true"					{ return symbol(sym.BOOLEAN, true); }
	"false"					{ return symbol(sym.BOOLEAN, false); }
	{Integer}				{ return symbol(sym.INTEGER, Integer.parseInt(yytext())); }
	
	\'						{ yybegin(STRING); string.setLength(0); }
	#						{ yybegin(ESCAPE); string.setLength(0); }
	
	{Identifier}			{ return symbol(sym.IDENTIFIER, yytext()); }
	{Invalid}				{ error("Illegal identifier"); return symbol(sym.IDENTIFIER, yytext()); }
	
	{Whitespace}			{ /* ignore */ }
}

<STRING> {
	\'						{ yybegin(YYINITIAL); return symbol(sym.STRING, string.toString()); }
	\'#						{ yybegin(ESCAPE); }
	
	{String}				{ string.append(yytext()); }
	\'\'					{ string.append('\''); }
	
	{LineTerminator}		{ fatal("String exceeds line"); }
}

<ESCAPE> {
	{Integer} / [\'#]		{ yybegin(BRANCH); parseEscape(); }
	{Integer}				{ yybegin(YYINITIAL); parseEscape(); return symbol(sym.STRING, string.toString()); }
}

<BRANCH> {
	\'						{ yybegin(STRING); }
	#						{ yybegin(ESCAPE); }
}

[^]							{ fatal("Illegal character \"" + yytext() + "\""); }
