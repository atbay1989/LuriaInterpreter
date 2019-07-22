package syntacticanalysis;

public class Token {
	
	final TokenType type;
	public final String lexeme;
	final Object literal;
	final int location;
	
	public Token(TokenType type, String lexeme, Object literal, int location) {
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.location = location;
	}
	
	public String toString() {
		return type + " " + lexeme + " " + literal;
 	}

}
