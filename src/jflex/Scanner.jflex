package com.ufcg.compiladores;

import java_cup.runtime.*;


%%

%class Lexer
%unicode
%ignorecase
%cup
%line
%column

%{
	StringBuilder string = new StringBuilder();

	private Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}
	
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
	
	private void error(String message) {
		ErrorCounter.increase();
		String output = String.format("(%d,%d) Error: %s", yyline+1, yycolumn+1, message);
		System.err.println(output);
	}
	
	private void fatal(String message) {
		String output = String.format("(%d,%d) Fatal: %s", yyline+1, yycolumn+1, message);
		System.err.println(output);
		System.exit(1);
	}
	
	private void parseEscape() {
		int code = Integer.parseInt(yytext());
		if(code > 255) error("Illegal char constant");
		string.append((char) code);
	}
%}

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
