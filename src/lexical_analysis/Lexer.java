/*
 * The Lexer class is responsible for the lexical analysis of the input source code passed as a String. It converts reserved
 * character sequences or lexemes, e.g. 'variable', '!=', into atomised tokens and adds them to an ArrayList, else it converts
 * them into STRING, NUMBER, or SIGNIFIER (variable or function identifier) tokens. The ArrayList<Token> represents the output
 * of the Lexer class. All characters the lexer will accept for the purpose of constructing tokens represent the 'alphabet' of
 * Luria as a formal language. In the class' fields, counters 'start' and 'current' orient the lexer when lexing the source code
 * string; 'reservedSequence' is a Map that pairs reserved character sequences, e.g. 'and' with their respective TokenType. 
 * 
 * */

package lexical_analysis;

import static lexical_analysis.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luria_interpreter.LuriaInterpreter;

public class Lexer {
	 final String sourceCode;
	 final List<Token> tokens = new ArrayList<>();
	 int start = 0;
	 int current = 0;
	 int location = 1;
	 static final Map<String, TokenType> reservedSequence;

	static {
		reservedSequence = new HashMap<>();
		reservedSequence.put("and", AND);
		reservedSequence.put("else", ELSE);
		reservedSequence.put("false", FALSE);
		reservedSequence.put("for", FOR);
		reservedSequence.put("function", FUNCTION);
		reservedSequence.put("if", IF);
		reservedSequence.put("null", NULL);
		reservedSequence.put("or", OR);
		reservedSequence.put("print", PRINT);
		reservedSequence.put("readboolean", READ_BOOLEAN);
		reservedSequence.put("readnumber", READ_NUMBER);
		reservedSequence.put("readstring", READ_STRING);
		reservedSequence.put("return", RETURN);
		reservedSequence.put("true", TRUE);
		reservedSequence.put("variable", VARIABLE);
		reservedSequence.put("while", WHILE);
	}

	public Lexer(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	/*
	 * process() increments the current counter and returns the next char to lex,
	 * i.e. it 'consumes' the current char.
	 */
	 char process() {
		current++;
		return sourceCode.charAt(current - 1);
	}

	/*
	 * alphabeticChar() checks whether a char is alphabetic, including upper and
	 * lower cases and '_'.
	 */
	 boolean alphabeticChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	/*
	 * numericChar() checks whether a char is numeric, '0' to '9'.
	 */
	 boolean numericChar(char c) {
		return c >= '0' && c <= '9';
	}

	/*
	 * alphanumericChar() checks whether a char is either alphabetic or numeric.
	 */
	 boolean alphanumericChar(char c) {
		return alphabeticChar(c) || numericChar(c);
	}

	/*
	 * addToken() receives a TokenType for construction of a Token at addToken().
	 * This method provides a null value to Tokens that do not have values that may
	 * be operated upon (non-terminal), i.e. a number or string value.
	 */
	 void addToken(TokenType type) {
		addToken(type, null);
	}

	/*
	 * addToken() constructs a Token object and adds it to the ArrayList<Token>. A
	 * Token has a.) a TokenType, b.) a value (e.g. 'null', '2', 'hello'), c.) a
	 * lexeme, or the string associated with the Token produced, (e.g. ';',
	 * 'hello'), and d.) the location, i.e. line count.
	 */
	 void addToken(TokenType type, Object literal) {
		String lexeme = sourceCode.substring(start, current);
		tokens.add(new Token(type, lexeme, literal, location));
	}

	/*
	 * match() 'consumes' the current char if that char is of the type expected by
	 * the calling method. For example, having lexed a '=' character, match checks
	 * whether the current char is of a known and reserved sequence, such as '=='.
	 * If not false, the current char is consumed and the lexer proceeds to
	 * construct a EQUAL_EQUAL Token, representing equality, else it constructs an
	 * EQUAL Token, representing assignment.
	 */
	 boolean match(char c) {
		if (end()) {
			return false;
		}
		if (sourceCode.charAt(current) != c) {
			return false;
		}
		current++;
		return true;
	}

	/*
	 * lookahead() returns the next char yet to be consumed, i.e. at 'current'. This
	 * enables the parser, for example, to conditionally process a sequence of
	 * unknown length, such as lexing a string.
	 */
	 char lookahead() {
		if (end()) {
			return '\0';
		}
		return sourceCode.charAt(current);
	}

	/*
	 * doubleLookahead() allows the lexer to observe the char beyond a floating
	 * point when lexing floating point numbers to confirm a number is well-formed,
	 * e.g. '1.0', not '1.a'.
	 */
	 char doubleLookahead() {
		if (current + 1 >= sourceCode.length()) {
			return '\0';
		}
		return sourceCode.charAt(current + 1);
	}

	/*
	 * end() checks whether the lexer has reached the end of the source code string.
	 */
	 boolean end() {
		return current >= sourceCode.length();
	}

	/*
	 * lexSourceCode() is the primary method of Lexer and returns its output, a
	 * List<Token>. All lexed programs end with an EOF Token.
	 */
	public List<Token> lexSourceCode() {
		while (!end()) {
			start = current;
			lexToken();
		}
		tokens.add(new Token(EOF, "", null, location));
		return tokens;
	}

	/*
	 * lexToken() is the body of the Lexer class, applying the above helper methods
	 * to conditionally decide, at its position in the source code, what Token to
	 * construct.
	 */
	 void lexToken() {
		char c = process();
		switch (c) {
		// Single char tokens.
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
		case '[':
			addToken(LEFT_BRACKET);
			break;
		case ']':
			addToken(RIGHT_BRACKET);
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
		case '%':
			addToken(MODULO);
			break;
		case '^':
			addToken(EXPONENT);
			break;
		// Many char tokens.
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
				while (lookahead() != '\n' && !end()) {
					process();
				}
			} else {
				addToken(FORWARD_SLASH);
			}
			break;
		// Whitespace. If new line, increment location, else ignore.
		case ' ':
		case '\r':
		case '\t':
			break;
		case '\n':
			location++;
			break;
		// String.
		case '"':
			lexString();
			break;
		// Number or Signifier, else unsupported.
		default:
			if (numericChar(c)) {
				lexNumber();
			} else if (alphabeticChar(c)) {
				lexSignifier();
			} else {
				LuriaInterpreter.lexerError(location, "unsupported character.");
			}
		}
	}

	/*
	 * lexNumber() lexes a number of unknown length upon encountering a numeric
	 * char. It checks for floating points and parses the numeric string as a Java
	 * double.
	 */
	 void lexNumber() {
		while (numericChar(lookahead())) {
			process();
		}
		if (lookahead() == '.' && numericChar(doubleLookahead())) {
			process();
			while (numericChar(lookahead())) {
				process();
			}
		}
		addToken(NUMBER, Double.parseDouble(sourceCode.substring(start, current)));
	}

	/*
	 * lexString() lexes a string of unknown length upon encountering a
	 * '"' char. If a new line char is found, location is incremented, if a closing '"
	 * ' is not encountered before the end of the source code, an error is given.
	 */
	 void lexString() {
		while (lookahead() != '"' && !end()) {
			if (lookahead() == '\n')
				location++;
			process();
		}
		if (end()) {
			LuriaInterpreter.lexerError(location, "string not closed.");
			return;
		}
		process();
		String value = sourceCode.substring(start + 1, current - 1);
		addToken(STRING, value);
	}

	/*
	 * lexSignifier() checks a char sequence not prefixed with a quotation mark '"'
	 * against the reserved sequences. If no type (null) is returned, it adds a
	 * Token of type SIGNIFIER.
	 */
	 void lexSignifier() {
		while (alphanumericChar(lookahead())) {
			process();
		}
		String text = sourceCode.substring(start, current);
		TokenType type = reservedSequence.get(text);
		if (type == null)
			type = SIGNIFIER;
		addToken(type);
	}

}
