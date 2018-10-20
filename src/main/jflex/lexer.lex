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

	/* o token em questao tera o valor especificado por value */
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
%}

Whitespace = [ \r\n\t]
Identifier = [:jletter:] [:jletterdigit:]*

RealNumber = [0-9]+"."+[0-9]


%%

<YYINITIAL> {

    /* Palavras reservadas */
    {ARRAY}                         { return symbol(sym.ARRAY);             }
    {BEGIN}	                        { return symbol(sym.BEGIN);             }
    {DO}                            { return symbol(sym.DO);                }
    {END}                           { return symbol(sym.END);               }
    {PROCEDURE}                     { return symbol(sym.PROCEDURE);         }
    {PROGRAM}                       { return symbol(sym.PROGRAM);           }
    {REPEAT}                        { return symbol(sym.REPEAT);            }
    {UNTIL}                         { return symbol(sym.UNTIL);             }
    {VAR}                           { return symbol(sym.VAR);               }


    /* Tipos nativos em Pascal */
    {INTEGER}                       { return symbol(sym.INTEGER);           }
    {BOOLEAN}                       { return symbol(sym.BOOLEAN);           }
    {REAL}                          { return symbol(sym.REAL);              }
    {STRING}                        { return symbol(sym.STRING);            }
    {CHAR}                          { return symbol(sym.CHAR);              }
    {TRUE}                          { return symbol(sym.TRUE);              }
    {FALSE}                         { return symbol(sym.FALSE);             }


    /* */
    ";"                             { return symbol(sym.SEMICOLON);         }
    "."                             { return symbol(sym.DOT);               }
    ":="                            { return symbol(sym.ASSIGNMENT);        }
    ":"                             { return symbol(sym.COLON);             }
    ","                             { return symbol(sym.COMMA);             }
    ".."                            { return symbol(sym.DOTDOT);            }


    /* relop */
    "="                             { return symbol(sym.EQUAL);             }
	">="                            { return symbol(sym.GE);                }
    ">"                             { return symbol(sym.GT);                }
    "<="                            { return symbol(sym.LE);                }
    "<"                             { return symbol(sym.LT);                }
    "<>"                            { return symbol(sym.NOTEQUAL);          }


    /* parentese & colchete */
    "("                             { return symbol(sym.LPAREN);            }
    "["                             { return symbol(sym.LBRAC);             }
    "]"                             { return symbol(sym.RBRAC);             }
    ")"                             { return symbol(sym.RPAREN);            }


    /* arilop */
    "-"                             { return symbol(sym.MINUS, yytext());   }
    {MOD}                           { return symbol(sym.MOD, yytext());     }
    "+"                             { return symbol(sym.PLUS, yytext());    }
    "/"                             { return symbol(sym.SLASH, yytext());   }
    "*"                             { return symbol(sym.STAR, yytext());    }


	{Identifier}			        { return symbol(sym.IDENTIFIER, yytext());  }

	{RealNumber}                    { return symbol(sym.REALNUMBER, yytext());  }

	{Whitespace}			        { /* ignore */                              }
}

[^]							        { throw new Error("Illegal character <" + yytext() + ">"); }