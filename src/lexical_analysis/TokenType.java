/*
 * TokenType is the site of the set of constants that signify the type of a Token. For those classes that require
 * reference to these types, namely the Lexer, Parser and LuriaInterpreter classes, a static import of TokenType
 * is made, allowing the constants use without specifying the TokenType container (enum) class. This is for the
 * purpose of improving code readability.
 * 
 * */

package lexical_analysis;

public enum TokenType {
/*	One char tokens.*/
	ASTERISK,
	COMMA,
	EXPONENT,
	FORWARD_SLASH,
	FULL_STOP,
	LEFT_BRACE,
	LEFT_BRACKET,
	LEFT_PARENTHESIS,
	MINUS,
	MODULO,
	PLUS,
	RIGHT_BRACE,
	RIGHT_BRACKET,
	RIGHT_PARENTHESIS,
	SEMI_COLON,
	
/*	One or many char tokens.*/
	EQUAL,
	EQUAL_EQUAL,
	EXCLAMATION,
	EXCLAMATION_EQUAL,
	GREATER,
	GREATER_EQUAL,
	LESS,
	LESS_EQUAL,
	
/*	Literal tokens.*/
	NUMBER,
	SIGNIFIER,
	STRING,
	
/*	ReservedSequence tokens.*/
	AND,
	ELSE,
	FALSE,
	FOR,
	FUNCTION,
	IF,
	NULL,
	OR,
	PRINT,
	READ_BOOLEAN,
	READ_NUMBER,
	READ_STRING,
	RETURN,
	THIS,
	TRUE,
	VARIABLE,
	WHILE,
	
/*	End of file token.*/
	EOF
	
}