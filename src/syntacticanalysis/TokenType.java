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
	EQUAL,
	EQUAL_EQUAL,
	
	// literal
	SIGNIFIER,
	STRING,
	NUMBER,
	
	// keyword
	AND,
	CLASS,
	ELSE,
	FALSE,
	TRUE,
	
	// end of file
	EOF
	
}
