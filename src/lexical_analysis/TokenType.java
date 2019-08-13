package lexical_analysis;

public enum TokenType {
/*	One character tokens.*/
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
	
/*	One or many character tokens.*/
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
	
/*	Reserved word tokens.*/
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
