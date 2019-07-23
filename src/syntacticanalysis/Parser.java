package syntacticanalysis;

import static syntacticanalysis.TokenType.*;

import java.util.ArrayList;
import java.util.List;

import luria.Luria;

public class Parser {
	
	private final List<Token> tokens;
	// pointer to current Token
	private int current = 0;
	
	// constructor
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	// entry method parse()
/*	public Expression parse() {
		try {
			return expression();
		} catch (ParserError error) {
			return null;
		}
	}*/
	public List<Statement> parse() {
		List<Statement> statements = new ArrayList<>();
		while (!end()) {
			statements.add(statement());
		}
		return statements;
	}

	// expression()
	private Expression expression() {
		return equality();
	}
	
	// statement()
	private Statement statement() {
		if (match(PRINT)) return printStatement();
		return expressionStatement();
	}
	
	// printStatement()
	private Statement printStatement() {
		Expression value = expression();
		consume(SEMI_COLON, "Error: Expect ; after value.");
		return new Statement.Print(value);
	}
	
	// expressionStatement() {
	private Statement expressionStatement() {
		Expression e = expression();
		consume(SEMI_COLON, "Error: Expect ; after expression.");
		return new Statement.ExpressionStatement(e);
	}
	
	// match()
	private boolean match(TokenType... types) {
		for (TokenType t : types) {
			if (check(t)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	// check()
	private boolean check(TokenType t) {
		if (end()) 
			return false;		
		return peek().type == t;
	}
	
	private Token advance() {
		if (!end())
			current++;
		return previous();
	}
	
	private boolean end() {
		return peek().type == EOF;
	}

	private Token peek() {
		return tokens.get(current);
	}
	
	private Token previous() {
		return tokens.get(current - 1);
	}
	
	// equality()
	private Expression equality() {
		Expression e = comparison();
		
		while (match(EXCLAMATION_EQUAL, EQUAL_EQUAL)) {
			Token operator = previous();
			Expression right = comparison();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}

	private Expression comparison() {
		Expression e = addition();
		
		while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
			Token operator = previous();
			Expression right = addition();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}

	private Expression addition() {
		Expression e = multiplication();

		while (match(PLUS, MINUS)) {
			Token operator = previous();
			Expression right = multiplication();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}

	private Expression multiplication() {
		Expression e = unary();

		while (match(FORWARD_SLASH, ASTERISK)) {
			Token operator = previous();
			Expression right = unary();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}

	private Expression unary() {
		if (match(EXCLAMATION, MINUS)) {
			Token operator = previous();
			Expression right = unary();
			return new Expression.Unary(operator, right);
		}
		return primary();
	}
	
	private Expression primary() {
		if (match(FALSE)) return new Expression.Literal(false);
		if (match(TRUE)) return new Expression.Literal(true);
		if (match(NIL)) return new Expression.Literal(null);
		if (match(NUMBER, STRING)) return new Expression.Literal(previous().literal);
		
		if (match(LEFT_PARENTHESIS)) {
			Expression e = expression();
			consume(RIGHT_PARENTHESIS, "Error: Expect ')' after expression.");
			return new Expression.Grouping(e);
		}
	    throw error(peek(), "Error: expect expression.");
	}

	private Token consume(TokenType type, String message) {
		if (check(type)) return advance();
		throw error(peek(), message);
	}

	private void synchronize() {
		advance();

		while (!end()) {
			if (previous().type == SEMI_COLON)
				return;

			switch (peek().type) {
			case CLASS:
			case FUNCTION:
			case VARIABLE:
			case FOR:
			case IF:
			case WHILE:
			case PRINT:
			case RETURN:
				return;
			}

			advance();
		}
	}
	
	// nested ParserError class
	private static class ParserError extends RuntimeException {}
	
	// ParserError helper
	private ParserError error(Token token, String message) {
		Luria.error(token, message);
		return new ParserError();
	}
	
}

