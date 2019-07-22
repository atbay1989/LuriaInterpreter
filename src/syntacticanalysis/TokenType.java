package syntacticanalysis;

public enum TokenType {
	// one character token
	LEFT_PARENTHESIS,
	RIGHT_PARENTHESIS,
	LEFT_BRACE,
	RIGHT_BRACE,
	COMMA,
	FULL_STOP,
	MINUS,
	PLUS,
	SEMI_COLON,
	FORWARD_SLASH,
	ASTERISK,
	
	// one or many character token
	EXCLAMATION,
	EXCLAMATION_EQUAL,
	EQUAL,
	EQUAL_EQUAL,
	GREATER,
	GREATER_EQUAL,
	LESS,
	LESS_EQUAL,
	
	// literal
	SIGNIFIER,
	STRING,
	NUMBER,
	
	// keyword
	AND,
	CLASS,
	ELSE,
	FALSE,
	FUNCTION,
	FOR,
	IF,
	NIL,
	OR,
	PRINT,
	RETURN,
	SUPER,
	THIS,
	TRUE,
	VARIABLE,
	WHILE,
	
	// end of file
	EOF
	
}
