package syntacticanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luria.Luria;

import static syntacticanalysis.TokenType.*;

public class Scanner {

	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int location = 1;

	private static final Map<String, TokenType> reservedWords;

	static {
		reservedWords = new HashMap<>();
		reservedWords.put("and", AND);
		reservedWords.put("class", CLASS);
		reservedWords.put("else", ELSE);
		reservedWords.put("false", FALSE);
		reservedWords.put("for", FOR);
		reservedWords.put("function", FUNCTION);
		reservedWords.put("if", IF);
		reservedWords.put("nil", NIL);
		reservedWords.put("or", OR);
		reservedWords.put("print", PRINT);
		reservedWords.put("return", RETURN);
		reservedWords.put("super", SUPER);
		reservedWords.put("this", THIS);
		reservedWords.put("variable", VARIABLE);
		reservedWords.put("while", WHILE);
	}

	public Scanner(String source) {
		this.source = source;
	}

	public List<Token> scanTokens() {
		while (!end()) {
			start = current;
			scanToken();
		}

		tokens.add(new Token(EOF, "", null, location));
		return tokens;
	}

	private void scanToken() {
		char c = advance();
		switch (c) {

		// one character token
		case '(':
			addToken(LEFT_PARENTHESIS);
			break;
		case ')':
			addToken(RIGHT_PARENTHESIS);
			break;
		case '{':
			addToken(LEFT_BRACE);
			break;
		case '}':
			addToken(RIGHT_BRACE);
			break;
		case ',':
			addToken(COMMA);
			break;
		case '.':
			addToken(FULL_STOP);
			break;
		case '-':
			addToken(MINUS);
			break;
		case '+':
			addToken(PLUS);
			break;
		case ';':
			addToken(SEMI_COLON);
			break;
		case '*':
			addToken(ASTERISK);
			break;

		// one or many character token
		case '!':
			addToken(match('=') ? EXCLAMATION_EQUAL : EXCLAMATION);
			break;
		case '=':
			addToken(match('=') ? EQUAL_EQUAL : EQUAL);
			break;
		case '<':
			addToken(match('=') ? LESS_EQUAL : LESS);
			break;
		case '>':
			addToken(match('=') ? GREATER_EQUAL : GREATER);
			break;
		case '/':
			if (match('/')) {
				while (peek() != '\n' && !end()) {
					advance();
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
			if (isDigit(c)) {
				number();
			} else if (isAlpha(c)) {
				identifier();
			} else {
				Luria.error(location, "Error: unexpect character.");
			}
		}
	}

	private void identifier() {
		while (isAlphanumeric(peek())) {
			advance();
		}
		// is reserved word?
		String text = source.substring(start, current);
		TokenType type = reservedWords.get(text);
		if (type == null)
			type = SIGNIFIER;
		addToken(type);
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	public boolean isAlphanumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	private boolean match(char expected) {
		if (end()) {
			return false;
		}
		if (source.charAt(current) != expected) {
			return false;
		}
		current++;
		return true;
	}

	private char peek() {
		if (end()) {
			return '\0';
		}

		return source.charAt(current);
	}

	private char peekNext() {
		if (current + 1 >= source.length()) {
			return '\0';
		}
		return source.charAt(current + 1);
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void number() {
		while (isDigit(peek())) {
			advance();
		}

		if (peek() == '.' && isDigit(peekNext())) {
			advance();

			while (isDigit(peek())) {
				advance();
			}
		}

		addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
	}

	private void string() {
		while (peek() != '"' && !end()) {
			if (peek() == '\n')
				location++;
			advance();

		}
		// missing "
		if (end()) {
			Luria.error(location, "Error: string not closed.");
			return;
		}
		// " found
		advance();
		// remove surrounding " and addToken()
		String value = source.substring(start + 1, current - 1);
		addToken(STRING, value);
	}

	private boolean end() {
		return current >= source.length();
	}

	private char advance() {
		current++;
		return source.charAt(current - 1);
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, location));
	}

}
