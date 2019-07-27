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
		//reservedSequence.put("class", CLASS);
		reservedSequence.put("else", ELSE);
		reservedSequence.put("false", FALSE);
		reservedSequence.put("for", FOR);
		reservedSequence.put("function", FUNCTION);
		reservedSequence.put("if", IF);
		reservedSequence.put("nil", NIL);
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
	
	// consume() increments the current counter and returns the next char to lex.
	private char consume() {
		current++;
		return sourceCode.charAt(current - 1);
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
		case '!': addToken(match('=') ? EXCLAMATION_EQUAL : EXCLAMATION);
			break;
		case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL);
			break;
		case '<': addToken(match('=') ? LESS_EQUAL : LESS);
			break;
		case '>': addToken(match('=') ? GREATER_EQUAL : GREATER);
			break;
		case '/':
			if (match('/')) {
				while (peek() != '\n' && !end()) {
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
			string();
			break;
		// default number, identifier, else error
		default:
			if (numericChar(c)) {
				number();
			} else if (alphabeticChar(c)) {
				signifier();
			} else {
				Luria.error(location, "Error: unexpected character.");
			}
		}
	}

/*	signifier() checks a char sequence not prefixed with a quotation mark (") against the reserved sequences.
	If no type (null) is returned, it adds a Token of type SIGNIFIER.*/
	private void signifier() {
		while (alphanumericChar(peek())) {
			consume();
		}
		String text = sourceCode.substring(start, current);
		TokenType type = reservedSequence.get(text);
		if (type == null)
			type = SIGNIFIER;
		addToken(type);
	}

	// alphabeticChar()
	private boolean alphabeticChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	// alphanumericChar()
	public boolean alphanumericChar(char c) {
		return alphabeticChar(c) || numericChar(c);
	}
	
	// numericChar()
	private boolean numericChar(char c) {
		return c >= '0' && c <= '9';
	}

	// number();
	private void number() {
		while (numericChar(peek())) {
			consume();
		}
		
		if (peek() == '.' && numericChar(peekNext())) {
			consume();

			while (numericChar(peek())) {
				consume();
			}
		}
		addToken(NUMBER, Double.parseDouble(sourceCode.substring(start, current)));
	}

	// string()
	private void string() {
		while (peek() != '"' && !end()) {
			if (peek() == '\n')
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

	private boolean match(char expected) {
		if (end()) {
			return false;
		}
		if (sourceCode.charAt(current) != expected) {
			return false;
		}
		current++;
		return true;
	}

	private char peek() {
		if (end()) {
			return '\0';
		}

		return sourceCode.charAt(current);
	}

	private char peekNext() {
		if (current + 1 >= sourceCode.length()) {
			return '\0';
		}
		return sourceCode.charAt(current + 1);
	}
	
	// end(). current >= length of sourceCode
	private boolean end() {
		return current >= sourceCode.length();
	}
	
}
