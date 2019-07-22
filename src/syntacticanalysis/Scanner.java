package syntacticanalysis;

import java.util.ArrayList;
import java.util.List;

import luria.Luria;

import static syntacticanalysis.TokenType.*;

public class Scanner {

	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int location = 1;

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
		default:
			Luria.error(location, "Error: unexpect character.");
		}
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
