/*
 * The Parser class is a recursive descent parser. In its fields are the list of Token objects lexed in-order by the lexer,
 * 'tokens', representing the parser's input, and a counter, 'current', that orients the parser as it parses the Token object
 * sequence. Available to the parser are a methods for _. These provide the infrastructure necessary for it to carry
 * out the process of constructing and nesting the syntax tree nodes, which are either of type Statement or Expression. The
 * output of the parser is a list of Statement objects, 'statements', which is declared at and returned from the parse() method.
 * */

package syntactic_analysis;

import java.util.ArrayList;
import java.util.List;

import lexical_analysis.Token;
import lexical_analysis.TokenType;
import luria_interpreter.LuriaInterpreter;

import static lexical_analysis.TokenType.*;

public class Parser {
	private final List<Token> tokens;
	private int current = 0;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

/*  The nested ParserError class is the object thrown by the parser and caught at parse().
 *  */
	private static class ParserError extends RuntimeException {}
	
/*  error() calls the Luria.parserError() method to which is passes the Token object at which the error occurred and a
 *  relevant error message.
 *  */
	private ParserError error(Token token, String error) {
		LuriaInterpreter.parserError(token, error);
		return new ParserError();
	}

/*  look() returns the current, yet to be processed Token in 'tokens'.
 *  */
	private Token look() {
		return tokens.get(current);
	}
	
/*  end() returns true if the current Token is of type EOF, i.e. the parser has reached the last Token in 'tokens'.
 *  */
	private boolean end() {
		return look().type == EOF;
	}

/*  previous() returns the Token object previous to the current.
 *  */
	private Token previous() {
		return tokens.get(current - 1);
	}	
	
/*  next() increments the 'current' count and returns the now previous Token object, i.e. it advances the parser.
 *  */	
	private Token next() {
		if (!end())
			current++;
		return previous();
	}
	
/*  process() 'consumes' expected Token objects, advancing the parser to the next Token. If an unexpected Token is
 *  encountered, process() throws an error composed of the current Token object's data and a relevant error message.
 *  */	
	private Token process(TokenType type, String error) {
		if (check(type)) return next();
		throw error(look(), error);
	}

/*  match() checks for each of the TokenTypes passed to it whether it is equal to that of the current Token object,
 *  by way of check(). If true, it advances the parser by way of next() and returns true. This allows the parser to
 *  determine what action to take according to the Token objects it encounters.
 *  */
	private boolean match(TokenType... types) {
		for (TokenType t : types) {
			if (check(t)) {
				next();
				return true;
			}
		}
		return false;
	}
	
	private void sync() {
		next();
		while (!end()) {
			if (previous().type == SEMI_COLON)
				return;
			switch (look().type) {
			case FUNCTION:
			case VARIABLE:
			case FOR:
			case IF:
			case WHILE:
			case PRINT:
			case RETURN:
			case READ_NUMBER:
				return;
			}
			next();
		}
	}
	
/*  check() is passed a TokenType and returns true if that TokenType is equal to that of the current Token object.
 *  This allows the parser to check for an expected, i.e. syntactically correct, Token object.
 *  */
	private boolean check(TokenType t) {
		if (end()) 
			return false;		
		return look().type == t;
	}
		
	
/*	This is the entry method of the parser and outputs a list of Statement objects or syntax tree nodes. 
 *  */
	public List<Statement> parse() {
		List<Statement> statements = new ArrayList<>();
		while (!end()) {
			statements.add(declaration());       
		}
		return statements;
	}

/*	declaration() is the first method the recursive decent parser calls and it checks for declarations of either
 *  variables or functions as signified by 'VARIABLE' or 'FUNCTION' TokenTypes. Else it calls, or descends, to 
 *  the statement() call, i.e. the top of Luria language grammar.
 *  */
	private Statement declaration() {
		try {
			if (match(VARIABLE))
				return variableDeclaration();
			if (match(FUNCTION))
				return functionDeclaration();
			return statement();
		} catch (ParserError error) {
			//next();
			sync();
			return null;
		}
	}
	
/*	variableDeclariation() instantiates a SIGNIFIER token object and if an EQUAL token is next encountered by the parser, returns a
 	Variable object with its initialisation expression, e.g. 'variable x = 1 + 2', else it returns an uninitialised Variable object
 	with no assigned expression, e.g. 'variable x;'*/
	private Statement variableDeclaration() {
		Token symbol = process(SIGNIFIER, "Invalid variable declaration.");
		Expression initialisation = null;
		if (match(EQUAL)) {
			initialisation = expression();
		}
		process(SEMI_COLON, "Invalid variable declaration. ';' expected after variable declaration.");
		return new Statement.VariableDeclaration(symbol, initialisation);
	}
	
	private Statement functionDeclaration() {
		Token symbol = process(SIGNIFIER, "Invalid function declaration");
		process(LEFT_PARENTHESIS, "'(' expected to open arguments.");
		List<Token> arguments = new ArrayList<>();
		if (!check(RIGHT_PARENTHESIS)) {
			do {
				arguments.add(process(SIGNIFIER, "Invalid argument."));
			} while (match(COMMA));
		}
		process(RIGHT_PARENTHESIS, "')' expected to close arguments.");
		process(LEFT_BRACE, "'{' expected to open block.");
		List<Statement> functionBlock = block();
		return new Statement.FunctionDeclaration(symbol, arguments, functionBlock);
	}
	
/*	expression() calls assignment().*/
	private Expression expression() {
		return assignment();
	}
	
/*	assignment().*/
	private Expression assignment() {
	    Expression e = or();  
		if (match(EQUAL)) {
			Token previous = previous();
			Expression value = assignment();
			if (e instanceof Expression.VariableExpression) {
				Token symbol = ((Expression.VariableExpression) e).symbol;
				return new Expression.Assignment(symbol, value);
			} else if (e instanceof Expression.Index) {
				Token symbol = ((Expression.Index) e).symbol;
				return new Expression.Allocation(e, symbol, value);
			}
			error(previous, "Invalid assignment.");
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

		while (match(FORWARD_SLASH, ASTERISK, MODULO, EXPONENT)) {
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
		Token symbol = previous();
		while (true) {
			if (match(LEFT_PARENTHESIS)) {	
				List<Expression> arguments = new ArrayList<>();
				if (!check(RIGHT_PARENTHESIS)) {
					do {
						arguments.add(expression());
					} while (match(COMMA));
				}
				Token rightParenthesis = process(RIGHT_PARENTHESIS, "')' expected to close arguments.");
				return new Expression.Call(e, rightParenthesis, arguments);
				
				//e = endCall(e);
			} else if (match(LEFT_BRACKET)) {
				//Expression array = literal();
				Expression array = expression();
				// Error checking? Consuming ']'.
				Token rightBracket = process(RIGHT_BRACKET, "expected ']' after index.");
				e = new Expression.Index(e, symbol, array);
			} else {
				break;
			}
		}
		return e;
	}
	
/*	endCall().*/
/*	private Expression endCall(Expression e) {
		List<Expression> arguments = new ArrayList<>();
		if (!check(RIGHT_PARENTHESIS)) {
			do {
				arguments.add(expression());
			} while (match(COMMA));
		}
		Token rightParenthesis = process(RIGHT_PARENTHESIS, "')' expected to close arguments.");
		return new Expression.Call(e, rightParenthesis, arguments);
	}*/

	/* literal(). */
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
			process(RIGHT_PARENTHESIS, "')' expected after expression.");
			return new Expression.Grouping(e);
		}
		if (match(LEFT_BRACKET)) {
			List<Expression> components = new ArrayList<>();
			if (match(RIGHT_BRACKET)) {
				return new Expression.Array(null);
			}
			if (!match(RIGHT_BRACKET)) {
				do {
					//Expression component = assignment();
					Expression component = expression();
					components.add(component);
				} while (match(COMMA));
			}
			process(RIGHT_BRACKET, "']' expected to close array declaration.");
			return new Expression.Array(components);
		}
		throw error(look(), "expression expected.");
	}
	
	// statement()
	private Statement statement() {
		if (match(IF))
			return ifStatement();
		if (match(WHILE))
			return whileStatement();
		if (match(PRINT))
			return printStatement();
		if (match(RETURN))
			return returnStatement();
		if (match(READ_NUMBER))
			return readNumberStatement();
		if (match(READ_STRING))
			return readStringStatement();
		if (match(READ_BOOLEAN))
			return readBooleanStatement();
		if (match(LEFT_BRACE))
			return new Statement.Block(block());
		return expressionStatement();
	}

	private Statement readBooleanStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ReadBoolean(value);
	}

	private Statement readStringStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ReadString(value);
	}

	private Statement readNumberStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ReadNumber(value);
	}

	private Statement returnStatement() {
		Token symbol = previous();
		Expression value = null;
		if (!check(SEMI_COLON)) {
			value = expression();
		}
		process(SEMI_COLON, "expecting ';' after return expression.");
		return new Statement.Return(symbol, value);
	}

	private Statement whileStatement() {
	    process(LEFT_PARENTHESIS, "'(' expected to start condition.");   
	    Expression condition = expression();                      
	    process(RIGHT_PARENTHESIS, "')' expected to end condition.");
	    Statement body = statement();
	    return new Statement.While(condition, body);
	}

	private Statement ifStatement() {
		process(LEFT_PARENTHESIS, "'(' expected to start condition.");
		Expression condition = expression();
		process(RIGHT_PARENTHESIS, "')' expected to end condition.");

		Statement thenBranch = statement();
		Statement elseBranch = null;
		if (match(ELSE)) {
			elseBranch = statement();
		}

		return new Statement.If(condition, thenBranch, elseBranch);
	}

/*  printStatement() constructs a Print object. Its Visitor is responsible for evaluating and 'printing' (System.out)
 *  the object's expression, i.e. 'value'.
 *  */
	private Statement printStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.Print(value);
	}
	
	// expressionStatement() {
	private Statement expressionStatement() {
		Expression e = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ExpressionStatement(e);
	}
	
	// block()
	private List<Statement> block() {
		List<Statement> block = new ArrayList<>();
		while (!check(RIGHT_BRACE) && !end()) {
			block.add(declaration());
		}
		process(RIGHT_BRACE, "'}' expected to end block.");
		return block;
	}

}

