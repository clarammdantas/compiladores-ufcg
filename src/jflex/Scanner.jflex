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
%}

Whitespace = [ \r\n\t]
StringCharacter = [^\r\n\']

Boolean = "true" | "false"
Integer = [0-9]+

Identifier = [_a-zA-Z][_a-zA-Z0-9]*

%state STRING

%%

<YYINITIAL> {
	"program"				{ return symbol(sym.PROGRAM); }
	"begin"					{ return symbol(sym.BEGIN); }
	"end"					{ return symbol(sym.END); }
	"var"					{ return symbol(sym.VARIABLE); }
	"function"				{ return symbol(sym.FUNCTION); }
	"procedure"				{ return symbol(sym.PROCEDURE); }
	
	"("						{ return symbol(sym.LEFT_PARENTHESIS); }
	")"						{ return symbol(sym.RIGHT_PARENTHESIS); }
	
	","						{ return symbol(sym.COMMA); }
	":"						{ return symbol(sym.COLON); }
	";"						{ return symbol(sym.SEMICOLON); }
	"."						{ return symbol(sym.FULL_STOP); }
	
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
	"%" | "mod"				{ return symbol(sym.MOD); }
	
	":="					{ return symbol(sym.ASSIGN); }
	
	{Boolean}				{ return symbol(sym.BOOLEAN, yytext()); }
	{Integer}				{ return symbol(sym.INTEGER, Integer.valueOf(yytext())); }
	
	\'						{ yybegin(STRING); string.setLength(0); }
	
	{Identifier}			{ return symbol(sym.IDENTIFIER, yytext()); }
	{Whitespace}			{ /* ignore */ }
}

<STRING> {
	\'						{ yybegin(YYINITIAL); return symbol(sym.STRING, string.toString()); }
	
	{StringCharacter}+		{ string.append(yytext()); }
	\'\'					{ string.append('\''); }
}

[^]							{ throw new Error("Illegal character <" + yytext() + ">"); }
