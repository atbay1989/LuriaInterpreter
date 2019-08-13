/*
 * The Token class is the token object produced by the lexing process. The data collated in a Token - its type, i.e. its
 * assigned meaning, the substring of the source code associated with it, its value (in the case of a number or string
 * literal), and its line location in the source code - are that which inform the parser how to construct nodes in the
 * syntax tree.
 * 
 * */

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
