package lexical_analysis;

public class Token {

	public final TokenType type;
	public final String lexeme;
	public final Object literal;
	public final int location;

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
