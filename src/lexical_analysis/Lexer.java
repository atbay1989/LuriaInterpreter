/*The Lexer class is responsible for the lexical analysis of the input source code passed as a String. It converts reserved
character sequences or lexemes, e.g. 'variable', ''!=' into atomised tokens and adds them to an ArrayList, else it converts
them into STRING, NUMBER, or SIGNIFIER (variable identifier) tokens. The Token ArrayList represents the output of the Lexer
class.*/

package lexical_analysis;

import static lexical_analysis.TokenType.*;

// Java imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Luria imports
import luria.Luria;

public class Lexer {
	// fields 
	private final String sourceCode;
	private final List<Token> tokens = new ArrayList<>();
	// start, the first char in the lexeme being lexed; current, the current char being lexed
	private int start = 0;
	private int current = 0;
	private int location = 1;
	// Map of reserved Strings and associated TokenType
	private static final Map<String, TokenType> reservedSequence;

	// construction of reservedSequence Map
	static {
		reservedSequence = new HashMap<>();
		reservedSequence.put("and", AND);
		reservedSequence.put("else", ELSE);
		reservedSequence.put("false", FALSE);
		reservedSequence.put("true", TRUE);
		reservedSequence.put("for", FOR);
		reservedSequence.put("function", FUNCTION);
		reservedSequence.put("if", IF);
		reservedSequence.put("null", NULL);
		reservedSequence.put("or", OR);
		reservedSequence.put("print", PRINT);
		reservedSequence.put("return", RETURN);
		reservedSequence.put("super", SUPER);
		reservedSequence.put("this", THIS);
		reservedSequence.put("variable", VARIABLE);
		reservedSequence.put("while", WHILE);
	}

	// constructor
	public Lexer(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
// These are the helper methods of the Lexer class.
	
	// consume() increments the current counter and returns the next char to lex.
	private char consume() {
		current++;
		return sourceCode.charAt(current - 1);
	}
	
	// alphabeticChar() checks whether a char is an alphabetic character or '_'.
	private boolean alphabeticChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	// numericChar() checks whether a char is a numeric digit.
	private boolean numericChar(char c) {
		return c >= '0' && c <= '9';
	}
	
	// alphanumericChar() checks whether a char is alphabetic or numeric.
	public boolean alphanumericChar(char c) {
		return alphabeticChar(c) || numericChar(c);
	}
	
	// addToken() 
	private void addToken(TokenType type) {
		addToken(type, null);
	}

	// addToken()
	private void addToken(TokenType type, Object literal) {
		String lexeme = sourceCode.substring(start, current);
		tokens.add(new Token(type, lexeme, literal, location));
	}
	
	// match()
	private boolean manyChar(char c) {
		if (end()) {
			return false;
		}
		if (sourceCode.charAt(current) != c) {
			return false;
		}
		current++;
		return true;
	}

	// peek()
	private char lookahead() {
		if (end()) {
			return '\0';
		}

		return sourceCode.charAt(current);
	}

	// peekNext()
	private char lookaheadNext() {
		if (current + 1 >= sourceCode.length()) {
			return '\0';
		}
		return sourceCode.charAt(current + 1);
	}
	
	// end(). current >= length of sourceCode
	private boolean end() {
		return current >= sourceCode.length();
	}
	
// These are the primary lexical analysis methods.	

	// lexSourceCode()
	public List<Token> lexSourceCode() {
		while (!end()) {
			start = current;
			lexToken();
		}
		// final Token lexed is always an end of file Token
		tokens.add(new Token(EOF, "", null, location));
		return tokens;
	}

	// lexToken()
	private void lexToken() {
		char c = consume();
		switch (c) {
		// one character token
		case '(': addToken(LEFT_PARENTHESIS);
			break;
		case ')': addToken(RIGHT_PARENTHESIS);
			break;
		case '{': addToken(LEFT_BRACE);
			break;
		case '}': addToken(RIGHT_BRACE);
			break;
		case '[': addToken(LEFT_BRACKET);
			break;
		case ']': addToken(RIGHT_BRACKET);
			break;
		case ',': addToken(COMMA);
			break;
		case '.': addToken(FULL_STOP);
			break;
		case '-': addToken(MINUS);
			break;
		case '+': addToken(PLUS);
			break;
		case ';': addToken(SEMI_COLON);
			break;
		case '*': addToken(ASTERISK);
			break;
		// one or many character token
		case '!': addToken(manyChar('=') ? EXCLAMATION_EQUAL : EXCLAMATION);
			break;
		case '=': addToken(manyChar('=') ? EQUAL_EQUAL : EQUAL);
			break;
		case '<': addToken(manyChar('=') ? LESS_EQUAL : LESS);
			break;
		case '>': addToken(manyChar('=') ? GREATER_EQUAL : GREATER);
			break;
		case '/':
			if (manyChar('/')) {
				while (lookahead() != '\n' && !end()) {
					consume();
				}
			} else {
				addToken(FORWARD_SLASH);
			}
			break;
		// whitespace
		case ' ':
		case '\r':
		case '\t':
			break;
		// new line
		case '\n':
			location++;
			break;
		// string
		case '"':
			lexString();
			break;
/*		A lexeme beginning with a numeric digit is assumed to be a number; a lexeme beginning with an alphabetic 
		character is assumed to be a signifier, i.e. a variable name.*/
		default:
			if (numericChar(c)) {
				lexNumber();
			} else if (alphabeticChar(c)) {
				lexSignifier();
			} else {
				Luria.error(location, "Error: unexpected character.");
			}
		}
	}
		
	// lexNumber() 
	private void lexNumber() {
		while (numericChar(lookahead())) {
			consume();
		}
		
		if (lookahead() == '.' && numericChar(lookaheadNext())) {
			consume();

			while (numericChar(lookahead())) {
				consume();
			}
		}
		addToken(NUMBER, Double.parseDouble(sourceCode.substring(start, current)));
	}

	// lexString()
	private void lexString() {
		while (lookahead() != '"' && !end()) {
			if (lookahead() == '\n')
				location++;
			consume();
		}
		if (end()) {
			Luria.error(location, "Error: string not closed.");
			return;
		}
		consume();
		String value = sourceCode.substring(start + 1, current - 1);
		addToken(STRING, value);
	}

	/*	lexSignifier() checks a char sequence not prefixed with a quotation mark (") against the reserved sequences.
	If no type (null) is returned, it adds a Token of type SIGNIFIER.*/
	private void lexSignifier() {
		while (alphanumericChar(lookahead())) {
			consume();
		}
		String text = sourceCode.substring(start, current);
		TokenType type = reservedSequence.get(text);
		if (type == null)
			type = SIGNIFIER;
		addToken(type);
	}
	
}
