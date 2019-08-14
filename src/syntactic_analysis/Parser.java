/*
 * The Parser class is a recursive descent parser. In its fields are the list of Token objects constructed in-order by the 
 * lexer, 'tokens', representing the parser's input, and a counter, 'current', that orients the parser as it parses the
 * Token object sequence. Available to the parser are a methods for useful actions such as a parser lookahead. These provide
 * the infrastructure necessary for it to carry out the process of constructing and nesting the syntax tree nodes, which are
 * either of type Statement or Expression. The output of the parser is a list of Statement objects, 'statements', which is
 * declared at and returned from the parse() method. The order of recursive method calls in the parser mirrors the production
 * rules of the Luria context-free grammar.
 * 
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

	/*
	 * The nested ParserError class is the object thrown by the parser and caught at
	 * parse(). It extends Java RuntimeException.
	 */
	private static class ParserError extends RuntimeException {
	}

	/*
	 * error() calls the Luria.parserError() method to which is passes the Token
	 * object at which the error occurred and a relevant error message.
	 */
	private ParserError error(Token token, String error) {
		LuriaInterpreter.parserError(token, error);
		return new ParserError();
	}

	/*
	 * lookahead() returns the current, yet to be processed Token in 'tokens'.
	 */
	private Token lookahead() {
		return tokens.get(current);
	}

	/*
	 * end() returns true if the current Token is of type EOF, i.e. the parser has
	 * reached the last Token in 'tokens'.
	 */
	private boolean end() {
		return lookahead().type == EOF;
	}

	/*
	 * previous() returns the Token object previous to the current.
	 */
	private Token lookprevious() {
		return tokens.get(current - 1);
	}

	/*
	 * next() increments the 'current' count and returns the now previous Token
	 * object, i.e. it advances the parser and is akin to process() in Lexer.
	 */
	private Token next() {
		if (!end())
			current++;
		return lookprevious();
	}

	/*
	 * check() is passed a TokenType and returns true if that TokenType is equal to
	 * that of the current Token object. This allows the parser to check for an
	 * expected, i.e. syntactically correct, Token object.
	 */
	private boolean check(TokenType t) {
		if (end())
			return false;
		return lookahead().type == t;
	}

	/*
	 * process() 'consumes' expected Token objects, advancing the parser to the next
	 * Token. If an unexpected Token is encountered, process() throws an error
	 * composed of the now current Token object's data and a relevant error message.
	 */
	private Token process(TokenType type, String error) {
		if (check(type))
			return next();
		throw error(lookahead(), error);
	}

	/*
	 * match() checks for each of the TokenTypes passed to it whether it is equal to
	 * that of the current Token object, by way of check(). If true, it advances the
	 * parser by way of next() and returns true, else false. This is the primary
	 * checking method of the parser.
	 */
	private boolean match(TokenType... types) {
		for (TokenType t : types) {
			if (check(t)) {
				next();
				return true;
			}
		}
		return false;
	}

	/*
	 * sync() is the means by which the parser recovers from the throwing of
	 * ParserError exceptions while parsing. sync() is called upon catch of a
	 * ParserError at declaration(), the method most commonly called (i.e. of lowest
	 * precedence). sync() then pursues a Token representing the next, able to be
	 * parsed statement and then advances the parser. Without sync() the parser
	 * potentially can become stuck in an infinite loop at parse() as the same
	 * erroneous Token continually throws errors.
	 */
	private void sync() {
		next();
		while (!end()) {
			if (lookprevious().type == SEMI_COLON)
				return;
			switch (lookahead().type) {
			case FUNCTION:
			case VARIABLE:
			case FOR:
			case IF:
			case WHILE:
			case PRINT:
			case RETURN:
			case READ_BOOLEAN:
			case READ_NUMBER:
			case READ_STRING:
				return;
			}
			next();
		}
	}

	/*
	 * This is the entry method of the parser and outputs a list of Statement
	 * objects or syntax tree nodes.
	 */
	public List<Statement> parse() {
		List<Statement> statements = new ArrayList<>();
		while (!end()) {
			statements.add(declaration());
		}
		return statements;
	}

	/*
	 * Declarations.
	 */

	/*
	 * declaration() checks for variable and function declarations and is analogous
	 * to the production rule in the context-free grammar of lowest precedence.
	 */
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

	/*
	 * variableDeclariation() instantiates a SIGNIFIER token object and if an EQUAL
	 * token is next encountered by the parser, returns a Variable object with its
	 * initialisation expression, e.g. 'variable x = 1 + 2', else it returns an
	 * uninitialised Variable object with no assigned expression, e.g. 'variable x;'
	 */
	private Statement variableDeclaration() {
		Token symbol = process(SIGNIFIER, "Invalid variable declaration.");
		Expression initialisation = null;
		if (match(EQUAL)) {
			initialisation = expression();
		}
		process(SEMI_COLON, "Invalid variable declaration. ';' expected after variable declaration.");
		return new Statement.VariableDeclaration(symbol, initialisation);
	}

	/*
	 * functionDeclariation() instantiates a SIGNIFIER token object and if a
	 * LEFT_PARENTHESIS token is next encountered by the parser, it proceeds to
	 * parse for function parameters, here 'arguments', separated by a ',' and
	 * closed with a ')'. It then parses for the function block.
	 */
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

	/*
	 * Statements.
	 */

	/*
	 * statement() manages the recursive calls to relevant statement methods.
	 */
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

	/*
	 * ifStatement() looks for an else before returning. Therefore an 'else'
	 * statement in Luria is always bound to the nearest 'if' statement preceding
	 * it. As a hand-written parser, there is opportunity here to avoid ambiguity
	 * when expressing such a control flow construct; at present, instead the
	 * convention from C has been implemented.
	 */
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

	/*
	 * whileStatement() constructs a 'while' node from a condition and statement
	 * (either a single statement or a block).
	 */
	private Statement whileStatement() {
		process(LEFT_PARENTHESIS, "'(' expected to start condition.");
		Expression condition = expression();
		process(RIGHT_PARENTHESIS, "')' expected to end condition.");
		Statement body = statement();
		return new Statement.While(condition, body);
	}

	/*
	 * printStatement() constructs a 'print' node, including the value to be
	 * printed.
	 */
	private Statement printStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.Print(value);
	}

	/*
	 * returnStatement() constructs a 'return' node, including the value to be
	 * return, else null value.
	 */
	private Statement returnStatement() {
		Token symbol = lookprevious();
		Expression value = null;
		if (!check(SEMI_COLON)) {
			value = expression();
		}
		process(SEMI_COLON, "';' expected after return expression.");
		return new Statement.Return(symbol, value);
	}

	/*
	 * readBooleanStatement() constructs a 'readboolean' node, including the value
	 * to be read.
	 */
	private Statement readBooleanStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ReadBoolean(value);
	}

	/*
	 * readStringStatement() constructs a 'readstring' node, including the value to
	 * be read.
	 */
	private Statement readStringStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ReadString(value);
	}

	/*
	 * readNumberStatement() constructs a 'readnumber' node, including the value to
	 * be read.
	 */
	private Statement readNumberStatement() {
		Expression value = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ReadNumber(value);
	}

	/*
	 * block() nests a body of statements, calling declaration() at the top of the
	 * parser.
	 */
	private List<Statement> block() {
		List<Statement> block = new ArrayList<>();
		while (!check(RIGHT_BRACE) && !end()) {
			block.add(declaration());
		}
		process(RIGHT_BRACE, "'}' expected to end block.");
		return block;
	}

	/*
	 * expressionStatement() allows for the evaluation of an expression where a
	 * statement is expected, e.g. calling a function that produces a side effect.
	 */
	private Statement expressionStatement() {
		Expression e = expression();
		process(SEMI_COLON, "';' expected to end statement.");
		return new Statement.ExpressionStatement(e);
	}

	/*
	 * Expressions.
	 */

	/*
	 * expression() recursively calls assignment().
	 */
	private Expression expression() {
		return assignment();
	}

	/*
	 * assignment() recursively calls or().
	 */
	private Expression assignment() {
		Expression e = or();
		if (match(EQUAL)) {
			Token previous = lookprevious();
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

	/*
	 * or() constructs a 'logical expression' node, consisting of an 'or' operator
	 * and its operands.
	 */
	private Expression or() {
		Expression e = and();
		while (match(OR)) {
			Token operator = lookprevious();
			Expression right = and();
			e = new Expression.Logical(e, operator, right);
		}
		return e;
	}

	/*
	 * and() constructs a 'logical expression' node, consisting of an 'or' operator
	 * and its operands.
	 */
	private Expression and() {
		Expression e = equality();
		while (match(AND)) {
			Token operator = lookprevious();
			Expression right = equality();
			e = new Expression.Logical(e, operator, right);
		}
		return e;
	}

	/*
	 * equality() constructs a 'binary expression' node, consisting of an operator
	 * and its operands. lookprevious() allows for the parser to retrieve the
	 * operator token, having called match(), enabling parsing of infix expressions.
	 * The order of operations for binary expressions are expressed by the order of
	 * recursive calls toward higher precedence, i.e. equality -> comparison() ->
	 * addition() -> multiplication() -> exponent().
	 */
	private Expression equality() {
		Expression e = comparison();
		while (match(EXCLAMATION_EQUAL, EQUAL_EQUAL)) {
			Token operator = lookprevious();
			Expression right = comparison();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}

	/*
	 * comparison().
	 */
	private Expression comparison() {
		Expression e = addition();
		while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
			Token operator = lookprevious();
			Expression right = addition();
			e = new Expression.Binary(e, operator, right);
		}
		return e;
	}

	/*
	 * addition().
	 */
	private Expression addition() {
		Expression e = multiplication();
		while (match(PLUS, MINUS)) {
			Token operator = lookprevious();
			Expression rightOperand = multiplication();
			e = new Expression.Binary(e, operator, rightOperand);
		}
		return e;
	}

	/*
	 * multiplication().
	 */
	private Expression multiplication() {
		Expression e = exponent();
		while (match(FORWARD_SLASH, ASTERISK, MODULO)) {
			Token operator = lookprevious();
			Expression rightOperand = exponent();
			e = new Expression.Binary(e, operator, rightOperand);
		}
		return e;
	}

	/*
	 * exponent().
	 */
	private Expression exponent() {
		Expression e = unary();
		while (match(EXPONENT)) {
			Token operator = lookprevious();
			Expression rightOperand = unary();
			e = new Expression.Binary(e, operator, rightOperand);
		}
		return e;
	}

	/*
	 * unary() constructs a 'unary' node, consisting of an operator and a literal
	 * expression.
	 */
	private Expression unary() {
		if (match(EXCLAMATION, MINUS)) {
			Token operator = lookprevious();
			Expression operand = literal();
			return new Expression.Unary(operator, operand);
		}
		return call();
	}

	/*
	 * call() constructs a 'function call' node, composed of the function SIGNIFIER
	 * token and its arguments. Else, it an 'array index' node.
	 */
	private Expression call() {
		Expression e = literal();
		Token symbol = lookprevious();
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

			} else if (match(LEFT_BRACKET)) {
				Expression array = expression();
				Token rightBracket = process(RIGHT_BRACKET, "expected ']' after index.");
				e = new Expression.Index(e, symbol, array);
			} else {
				break;
			}
		}
		return e;
	}

	/*
	 * literal() concerns the terminals of the language, including Boolean values,
	 * numbers and strings. It is also the site at which combinations, i.e.
	 * expressions enclosed within parenthesis, and arrays are parsed.
	 */
	private Expression literal() {
		if (match(FALSE))
			return new Expression.Literal(false);
		if (match(TRUE))
			return new Expression.Literal(true);
		if (match(NULL))
			return new Expression.Literal(null);
		if (match(NUMBER, STRING))
			return new Expression.Literal(lookprevious().literal);
		if (match(SIGNIFIER)) {
			return new Expression.VariableExpression(lookprevious());
		}
		if (match(LEFT_PARENTHESIS)) {
			Expression e = expression();
			process(RIGHT_PARENTHESIS, "')' expected after expression.");
			return new Expression.Combination(e);
		}
		if (match(LEFT_BRACKET)) {
			List<Expression> components = new ArrayList<>();
			if (match(RIGHT_BRACKET)) {
				return new Expression.Array(null);
			}
			if (!match(RIGHT_BRACKET)) {
				do {
					Expression component = expression();
					components.add(component);
				} while (match(COMMA));
			}
			process(RIGHT_BRACKET, "']' expected to close array declaration.");
			return new Expression.Array(components);
		}
		throw error(lookahead(), "expression expected.");
	}

}
