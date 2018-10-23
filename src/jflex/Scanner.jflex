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
	
	private void appendEscape() {
		appendEscape(0);
	}
	
	private void appendEscape(int cut) {
		String text = yytext();
		text = text.substring(0, text.length() + cut);
		string.append((char) Integer.parseInt(text));		
	}
%}

Whitespace = [ \r\n\t]
String = [^\r\n\']+

Boolean = "true" | "false"
Integer = [0-9]+

Identifier = [_a-zA-Z][_a-zA-Z0-9]*

%state STRING, ESCAPE

%%

<YYINITIAL> {
	"program"				{ return symbol(sym.PROGRAM); }
	"begin"					{ return symbol(sym.BEGIN); }
	"end"					{ return symbol(sym.END); }
	"var"					{ return symbol(sym.VARIABLE); }
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
	
	{Boolean}				{ return symbol(sym.BOOLEAN, yytext()); }
	{Integer}				{ return symbol(sym.INTEGER, Integer.parseInt(yytext())); }
	
	\'						{ yybegin(STRING); string.setLength(0); }
	#						{ yybegin(ESCAPE); string.setLength(0); }
	
	{Identifier}			{ return symbol(sym.IDENTIFIER, yytext()); }
	{Whitespace}			{ /* ignore */ }
}

<STRING> {
	\'						{ yybegin(YYINITIAL); return symbol(sym.STRING, string.toString()); }
	\'#						{ yybegin(ESCAPE); }
	
	{String}				{ string.append(yytext()); }
	\'\'					{ string.append('\''); }
}

<ESCAPE> {
	{Integer}#				{ appendEscape(-1); }
	{Integer}\'				{ yybegin(STRING); appendEscape(-1); }
	{Integer}				{ yybegin(YYINITIAL); appendEscape(); return symbol(sym.STRING, string.toString()); }
}

[^]							{ throw new Error("Illegal character <" + yytext() + ">"); }
