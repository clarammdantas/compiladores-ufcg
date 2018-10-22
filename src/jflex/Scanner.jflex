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
	private Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}
	
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
%}

Whitespace = [ \r\n\t]
Identifier = [_a-zA-Z][_a-zA-Z0-9]*

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
	
	"true" | "false"		{ return symbol(sym.BOOL, yytext()); }
	
	{Identifier}			{ return symbol(sym.IDENTIFIER, yytext()); }
	{Whitespace}			{ /* ignore */ }
}

[^]							{ throw new Error("Illegal character <" + yytext() + ">"); }
