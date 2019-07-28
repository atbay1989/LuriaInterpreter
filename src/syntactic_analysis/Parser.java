package syntactic_analysis;

/*Java imports.*/
import java.util.ArrayList;
import java.util.List;

/*Luria imports.*/
import luria.Luria;
import lexical_analysis.Token;
import lexical_analysis.TokenType;
import static lexical_analysis.TokenType.*;

public class Parser {
/*	Fields. 'tokens' represents the Parser input, a List of Token objects, 'current' the Parser position counter.*/
	private final List<Token> tokens;
	private int current = 0;
	
/*	Constructor.*/
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

/*	parse() returns the output of the Parser as a List of Statement objects. It initiates the parsing procedure until the EOF token
	is encountered.*/
	public List<Statement> parse() {
		List<Statement> statements = new ArrayList<>();
		while (!end()) {
			statements.add(declaration());       
		}
		return statements;
	}

/*	declaration() */
	private Statement declaration() {
		try {
			if (match(VARIABLE))
				return variableDeclaration();
			return statement();
		} catch (ParserError error) {
			synchronize();
			return null;
		}
	}
	
	// statement()
	private Statement statement() {
		if (match(IF)) return ifStatement(); 
		if (match(WHILE)) return whileStatement();
		if (match(PRINT)) return printStatement();		
		if (match(LEFT_BRACE)) return new Statement.Block(block());
		return expressionStatement();
	}
	
	// expression()
	private Expression expression() {
		return assignment();
	}
	
	private Statement whileStatement() {
	    consume(LEFT_PARENTHESIS, "Error: Expect ( after 'while'.");   
	    Expression condition = expression();                      
	    consume(RIGHT_PARENTHESIS, "Error: expect ) after condition.");
	    Statement body = statement();

	    return new Statement.While(condition, body);
	}

	private Statement ifStatement() {
		consume(LEFT_PARENTHESIS, "Error: Expect ( after if.");
		Expression condition = expression();
		consume(RIGHT_PARENTHESIS, "Error: Expect ) after if condition.");

		Statement thenBranch = statement();
		Statement elseBranch = null;
		if (match(ELSE)) {
			elseBranch = statement();
		}

		return new Statement.If(condition, thenBranch, elseBranch);
	}

	// printStatement()
	private Statement printStatement() {
		Expression value = expression();
		consume(SEMI_COLON, "Error: Expect ; after value.");
		return new Statement.Print(value);
	}
	
	// variableDeclariation()
	private Statement variableDeclaration() {
		Token name = consume(SIGNIFIER, "Error: Expect variable name.");

		Expression initializer = null;
		if (match(EQUAL)) {
			initializer = expression();
		}

		consume(SEMI_COLON, "Error: Expect ; after variable declaration.");
		return new Statement.Variable(name, initializer);
	}   
	
	// expressionStatement() {
	private Statement expressionStatement() {
		Expression e = expression();
		consume(SEMI_COLON, "Error: Expect ; after expression.");
		return new Statement.ExpressionStatement(e);
	}
	
	// block()
	private List<Statement> block() {
		List<Statement> statements = new ArrayList<>();
		
		while (!check(RIGHT_BRACE) && !end()) {
			statements.add(declaration());
		}
		consume(RIGHT_BRACE, "Error: Expected } after block.");
		return statements;
	}
	
	// assignment()
	private Expression assignment() {
	    Expression e = or();  
		if (match(EQUAL)) {
			Token result = previous();
			Expression value = assignment();
			
			if (e instanceof Expression.VariableExpression) {
				Token symbol = ((Expression.VariableExpression) e).symbol;
				return new Expression.Assignment(symbol, value);
			}
			
			error(result, "Error: Invalid assignment.");
		}
		return e;
	}
	
	private Expression or() {
	    Expression e = and();
	    while (match(OR)) {                              
	      Token operator = previous();                   
	      Expression right = and();                            
	      e = new Expression.Logical(e, operator, right);
	    }                                                
	    return e;  
	}

	private Expression and() {
	    Expression e = equality();

	    while (match(AND)) {                             
	      Token operator = previous();                   
	      Expression right = equality();                       
	      e = new Expression.Logical(e, operator, right);
	    }                                                
	    return e; 
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
	    if (match(SIGNIFIER)) {                      
	        return new Expression.VariableExpression(previous());       
	    }    
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
	
// This is the nested ParserError class.
	private static class ParserError extends RuntimeException {}
	
	// ParserError() is a helper method.
	private ParserError error(Token token, String message) {
		Luria.error(token, message);
		return new ParserError();
	}
	
}

