package com.ufcg.compiladores;

import java_cup.runtime.*;


%%

%class Lexer
%unicode
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
Identifier = [:jletter:] [:jletterdigit:]*

%%

<YYINITIAL> { 
	{Identifier}			{ return symbol(sym.IDENTIFIER, yytext()); }
	{Whitespace}			{ /* ignore */ }
}

[^]							{ throw new Error("Illegal character <" + yytext() + ">"); }