/*The Parser class is responsible for parsing or _ a context-free List of Token objects, representing the source code as lexed. Each non-terminal
  expression has a method routine.*/

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

/*  The nested ParserError class.*/
	private static class ParserError extends RuntimeException {}
	
/*Parser helper methods.*/

	/* error() calls the Luria error() method and returns a ParserError object.*/
	private ParserError error(Token token, String error) {
		Luria.error(token, error);
		return new ParserError();
	}
	
	private Token advance() {
		if (!end())
			current++;
		return previous();
	}
	
	private Token consume(TokenType type, String error) {
		if (check(type)) return advance();
		throw error(peek(), error);
	}
	
	private Token peek() {
		return tokens.get(current);
	}
	
	private Token previous() {
		return tokens.get(current - 1);
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
	
	private boolean end() {
		return peek().type == EOF;
	}
	
	private void sync() {
		advance();
		while (!end()) {
			if (previous().type == SEMI_COLON)
				return;
			switch (peek().type) {
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
	
/*	parse() returns the output of the Parser as a List of Statement objects. It calls the top parsing procedure until the EOF token
	is encountered.*/
	public List<Statement> parse() {
		List<Statement> statements = new ArrayList<>();
		while (!end()) {
			statements.add(declaration());       
		}
		return statements;
	}

/*	declaration() checks for a VARIABLE token and calls the variable declaration procedure, else the statement procedure.*/
	private Statement declaration() {
		try {
			if (match(VARIABLE))
				return variableDeclaration();
			if (match(FUNCTION))
				return functionDeclaration();
			return statement();
		} catch (ParserError error) {
			sync();
			return null;
		}
	}
	
/*	variableDeclariation() instantiates a SIGNIFIER token object and if an EQUAL token is next encountered by the parser, returns a
 	Variable object with its initialisation expression, e.g. 'variable x = 1 + 2', else it returns an uninitialised Variable object
 	with no assigned expression, e.g. 'variable x;'*/
	private Statement variableDeclaration() {
		Token symbol = consume(SIGNIFIER, "Error: Invalid variable declaration.");
		Expression initialisation = null;
		if (match(EQUAL)) {
			initialisation = expression();
		}
		consume(SEMI_COLON, "Error: Invalid variable declaration. ';' expected after variable declaration.");
		return new Statement.Variable(symbol, initialisation);
	}
	
	private Statement functionDeclaration() {
		Token symbol = consume(SIGNIFIER, "Error: Invalid function declaration");
		consume(LEFT_PARENTHESIS, "Error: '(' expected to open arguments.");
		List<Token> arguments = new ArrayList<>();
		if (!check(RIGHT_PARENTHESIS)) {
			do {
				arguments.add(consume(SIGNIFIER, "Error: Invalid argument."));
			} while (match(COMMA));
		}
		consume(RIGHT_PARENTHESIS, "Error: ')' expected to close arguments.");
		consume(LEFT_BRACE, "Error: '{' expected to open block.");
		List<Statement> functionBlock = block();
		return new Statement.Function(symbol, arguments, functionBlock);
	}
	
/*	expression() calls assignment().*/
	private Expression expression() {
		return assignment();
	}
	
/*	assignment().*/
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
	
/*	or().*/
	private Expression or() {
	    Expression e = and();
	    while (match(OR)) {                              
	      Token operator = previous();                   
	      Expression right = and();                            
	      e = new Expression.Logical(e, operator, right);
	    }                                                
	    return e;  
	}
	
/*	and().*/
	private Expression and() {
	    Expression e = equality();
	    while (match(AND)) {                             
	      Token operator = previous();                   
	      Expression right = equality();                       
	      e = new Expression.Logical(e, operator, right);
	    }                                                
	    return e; 
	}
	
/*	equality().*/
	private Expression equality() {
		Expression e = comparison();
		while (match(EXCLAMATION_EQUAL, EQUAL_EQUAL)) {
			Token operator = previous();
			Expression right = comparison();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}
	
/*	comparison().*/
	private Expression comparison() {
		Expression e = additionSubtraction();	
		while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
			Token operator = previous();
			Expression right = additionSubtraction();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}
	
/*	additionSubtraction().*/	
	private Expression additionSubtraction() {
		Expression e = multiplicationDivision();

		while (match(PLUS, MINUS)) {
			Token operator = previous();
			Expression rightOperand = multiplicationDivision();
			e = new Expression.Binary(e, operator, rightOperand);
		}
		return e;
	}

/*	multiplicationDivision().*/
	private Expression multiplicationDivision() {
		Expression e = unary();

		while (match(FORWARD_SLASH, ASTERISK)) {
			Token operator = previous();
			Expression rightOperand = unary();
			e = new Expression.Binary(e, operator, rightOperand);
		}
		return e;
	}
	
/*	unary() checks for the presence of '!' or '-' unary operators. If present, a Unary object consisting of the operator
  	and operand is returned, else literal procedure is called.*/	
	private Expression unary() {
		if (match(EXCLAMATION, MINUS)) {
			Token operator = previous();
			Expression operand = unary();
			return new Expression.Unary(operator, operand);
		}
		//return literal();
		return call();
	}

/*	call().*/
	private Expression call() {
		Expression e = literal();
		while (true) {
			if (match(LEFT_PARENTHESIS)) {
				e = endCall(e);
			} else {
				break;
			}
		}
		return e;
	}

/*	endCall().*/
	private Expression endCall(Expression e) {
		List<Expression> arguments = new ArrayList<>();
		if (!check(RIGHT_PARENTHESIS)) {
			do {
				arguments.add(expression());
			} while (match(COMMA));
		}
		Token rightParenthesis = consume(RIGHT_PARENTHESIS, "Error: ')' expected to close arguments.");
		return new Expression.Call(e, rightParenthesis, arguments);
	}

/*	literal().*/
	private Expression literal() {
		if (match(FALSE))
			return new Expression.Literal(false);
		if (match(TRUE))
			return new Expression.Literal(true);
		if (match(NULL))
			return new Expression.Literal(null);
		if (match(NUMBER, STRING))
			return new Expression.Literal(previous().literal);
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
	
	// statement()
	private Statement statement() {
		if (match(IF))
			return ifStatement();
		if (match(WHILE))
			return whileStatement();
		if (match(PRINT))
			return printStatement();
		if (match(LEFT_BRACE))
			return new Statement.Block(block());
		return expressionStatement();
	}

	private Statement whileStatement() {
	    consume(LEFT_PARENTHESIS, "Error: '(' expected to start condition.");   
	    Expression condition = expression();                      
	    consume(RIGHT_PARENTHESIS, "Error: ')' expected to end condition.");
	    Statement body = statement();
	    return new Statement.While(condition, body);
	}

	private Statement ifStatement() {
		consume(LEFT_PARENTHESIS, "Error: '(' expected to start condition.");
		Expression condition = expression();
		consume(RIGHT_PARENTHESIS, "Error: ')' expected to end condition.");

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
		consume(SEMI_COLON, "Error: ';' expected to end statement.");
		return new Statement.Print(value);
	}
	
	// expressionStatement() {
	private Statement expressionStatement() {
		Expression e = expression();
		consume(SEMI_COLON, "Error: ';' expected to end statement.");
		return new Statement.ExpressionStatement(e);
	}
	
	// block()
	private List<Statement> block() {
		List<Statement> block = new ArrayList<>();
		while (!check(RIGHT_BRACE) && !end()) {
			block.add(declaration());
		}
		consume(RIGHT_BRACE, "Error: '}' expected to end block.");
		return block;
	}

}

