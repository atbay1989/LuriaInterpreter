/*
 * The Interpreter class implements the Expression and Statement interfaces and therefore defines all methods
 * for interpreting nodes in the syntax tree. Interpreter is an implementation of a tree-walk interpreter. An
 * Interpreter instance has reference to its MemoryEnvironment and a constant reference to 'global' scope.
 * 
 * */

package interpretation;

import static lexical_analysis.TokenType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lexical_analysis.Token;
import luria_interpreter.LuriaInterpreter;
import syntactic_analysis.Expression;
import syntactic_analysis.Statement;
import syntactic_analysis.Expression.Allocation;
import syntactic_analysis.Expression.Array;
import syntactic_analysis.Expression.Assignment;
import syntactic_analysis.Expression.Binary;
import syntactic_analysis.Expression.Call;
import syntactic_analysis.Expression.Combination;
import syntactic_analysis.Expression.Index;
import syntactic_analysis.Expression.Literal;
import syntactic_analysis.Expression.Logical;
import syntactic_analysis.Expression.Unary;
import syntactic_analysis.Expression.VariableExpression;
import syntactic_analysis.Statement.Block;
import syntactic_analysis.Statement.FunctionDeclaration;
import syntactic_analysis.Statement.If;
import syntactic_analysis.Statement.Print;
import syntactic_analysis.Statement.ReadBoolean;
import syntactic_analysis.Statement.ReadNumber;
import syntactic_analysis.Statement.ReadString;
import syntactic_analysis.Statement.Return;
import syntactic_analysis.Statement.VariableDeclaration;
import syntactic_analysis.Statement.While;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {
	public final MemoryEnvironment globalScope = new MemoryEnvironment();
	private MemoryEnvironment environment = globalScope;

	/*
	 * interpret() calls execute() upon each Statement object in List<Statement>. It
	 * is at this point in the Java call stack that an InterpreterError is caught.
	 */
	public void interpret(List<Statement> statements) {
		try {
			for (Statement s : statements) {
				execute(s);
			}
		} catch (InterpreterError error) {
			LuriaInterpreter.interpreterError(error);
		}
	}

	/*
	 * execute() applies the Visitor pattern, passing this instance to a given
	 * Statement node and, in turn, calling the relevant method by way of dynamic
	 * dispatch.
	 */
	private void execute(Statement s) {
		s.accept(this);
	}

	/*
	 * evaluate() applies the Visitor pattern, passing this instance to a given
	 * Expression node and, in turn, calling the relevant method by way of dynamic
	 * dispatch.
	 */
	private Object evaluate(Expression e) {
		return e.accept(this);
	}

	/*
	 * executeBlock() is passed the nested List<Statement> and the new
	 * MemoryEnvironment instantiated at visitBlockStatement(). For duration of
	 * execution of the block's statements, the current (this) environment is stored
	 * in a variable, 'previous', and the block's environment applied. The
	 * interpreter is then returned to the previous environment
	 */
	public void executeBlock(List<Statement> statements, MemoryEnvironment environment) {
		MemoryEnvironment previous = this.environment;
		try {
			this.environment = environment;
			for (Statement s : statements) {
				execute(s);
			}

		} finally {
			this.environment = previous;
		}
	}

	/*
	 * checkOperand() checks whether a unary operand is of type Java Double.
	 * Arithmetic in the Luria interpreter is handled in doubles.
	 */
	private void checkOperand(Token operator, Object operand) {
		if (operand instanceof Double)
			return;
		throw new InterpreterError(operator, "Operand must be a number.");
	}

	/*
	 * checkOperand() checks whether both binary arithmetic operands are of type
	 * Java Double.
	 */
	private void checkOperands(Token operator, Object left, Object right) {
		if (left instanceof Double && right instanceof Double)
			return;
		throw new InterpreterError(operator, "Operands must be numbers.");
	}

	/*
	 * truthy() tests for the truth of Luria expressions. truthy() returns false in
	 * case of a false or null value, else true.
	 */
	private boolean truthy(Object o) {
		if (o == null)
			return false;
		if (o instanceof Boolean)
			return (boolean) o;
		return true;
	}

	/*
	 * equal() tests for equality of Luria expressions. In Luria, null is only equal
	 * to null.
	 */
	private boolean equal(Object l, Object r) {
		if (l == null && r == null)
			return true;
		if (l == null)
			return false;
		return l.equals(r);
	}

	/*
	 * stringify() presents whole number doubles as integers, removing '.0' before
	 * printing.
	 */
	private String stringify(Object o) {
		if (o == null)
			return "null";
		if (o instanceof Double) {
			String t = o.toString();
			if (t.endsWith(".0")) {
				t = t.substring(0, t.length() - 2);
			}
			return t;
		}
		return o.toString();
	}

	/*
	 * visitBinaryExpression() recursively calls on and evaluates the children of a
	 * binary node. Evaluation is carried out using Java equivalents to Luria
	 * operations.
	 */
	@Override
	public Object visitBinaryExpression(Binary e) {
		Object left = evaluate(e.left);
		Object right = evaluate(e.right);
		switch (e.operator.type) {
		// Arithmetic.
		case PLUS:
			if (left instanceof Double && right instanceof Double) {
				return (double) left + (double) right;
			}

			if (left instanceof String && right instanceof String) {
				return (String) left + (String) right;
			}
			throw new InterpreterError(e.operator, "Error: Operands must be of same type.");
		case MINUS:
			checkOperands(e.operator, left, right);
			return (double) left - (double) right;
		case FORWARD_SLASH:
			checkOperands(e.operator, left, right);
			return (double) left / (double) right;
		case ASTERISK:
			checkOperands(e.operator, left, right);
			return (double) left * (double) right;
		case MODULO:
			checkOperands(e.operator, left, right);
			return (double) left % (double) right;
		case EXPONENT:
			checkOperands(e.operator, left, right);
			return Math.pow((double) left, (double) right);
		// Comparison operations.
		case GREATER:
			checkOperands(e.operator, left, right);
			return (double) left > (double) right;
		case GREATER_EQUAL:
			checkOperands(e.operator, left, right);
			return (double) left >= (double) right;
		case LESS:
			checkOperands(e.operator, left, right);
			return (double) left < (double) right;
		case LESS_EQUAL:
			checkOperands(e.operator, left, right);
			return (double) left <= (double) right;
		// Equality.
		case EXCLAMATION_EQUAL:
			return !equal(left, right);
		case EQUAL_EQUAL:
			return equal(left, right);
		}
		return null;
	}

	/*
	 * visitCombinationExpression() returns evaluation of the combined expression.
	 */
	@Override
	public Object visitCombinationExpression(Combination e) {
		return evaluate(e.expression);
	}

	/*
	 * visitCombinationExpression() returns the terminal value;
	 */
	@Override
	public Object visitLiteralExpression(Literal e) {
		return e.value;
	}

	/*
	 * visitCombinationExpression() evaluates the operand. If a '-' is encountered,
	 * the operand is negated and returned. If an '!' is encountered it is evaluated
	 * for truth by way of truthy(), negated and returned.
	 */
	@Override
	public Object visitUnaryExpression(Unary e) {
		Object right = evaluate(e.right);
		switch (e.operator.type) {
		case MINUS:
			checkOperand(e.operator, right);
			return -(double) right;
		case EXCLAMATION:
			return !truthy(right);
		}
		return null;
	}

	/*
	 * visitExpressionStatement() evaluates the expression of the statement.
	 */
	@Override
	public Void visitExpressionStatement(Statement.ExpressionStatement statement) {
		evaluate(statement.expression);
		return null;
	}

	/*
	 * visitVariableDeclarationStatement() checks for an assignment value
	 * expression. If null a variable is stored in the MemoryEnvironment by way of
	 * store() with a null value. Else, the expression is evaluated and stored with
	 * the declared variable.
	 */
	@Override
	public Void visitVariableDeclarationStatement(VariableDeclaration statement) {
		Object value = null;
		if (statement.initialisation != null) {
			value = evaluate(statement.initialisation);
		}
		environment.store(statement.symbol.lexeme, value);
		return null;
	}

	/*
	 * visitBlockStatement() calls executeBlock() passing a new MemoryEnvironment
	 * for this block.
	 */
	@Override
	public Void visitBlockStatement(Block statement) {
		executeBlock(statement.statements, new MemoryEnvironment(environment));
		return null;
	}

	/*
	 * visitFunctionDeclarationStatement() constructs a Function object with a
	 * FunctionDeclaration object consisting of a signifier, parameters and a
	 * function body of statements. This is then stored in the interpreter
	 * environment.
	 */
	@Override
	public Void visitFunctionDeclarationStatement(FunctionDeclaration statement) {
		interpretation.Function function = new interpretation.Function(statement, environment);
		environment.store(statement.symbol.lexeme, function);
		return null;
	}

	/*
	 * visitIfStatement() mirrors the form of the Luria if else statement in Java.
	 * See comment at Parser.ifStatement().
	 */
	@Override
	public Void visitIfStatement(If statement) {
		if (truthy(evaluate(statement.condition))) {
			execute(statement.thenBranch);
		} else if (statement.elseBranch != null) {
			execute(statement.elseBranch);
		}
		return null;
	}

	/*
	 * visitLogicalExpression() evaluates the left operand first. If evaluated to
	 * true at OR it short circuits, returning left. If evaluated to false at AND it
	 * short circuits, returning left. Otherwise the right is then evaluated.
	 */
	@Override
	public Object visitLogicalExpression(Logical expression) {
		Object left = evaluate(expression.left);
		if (expression.operator.type == OR) {
			if (truthy(left))
				return left;
		} else {
			if (!truthy(left))
				return left;
		}
		return evaluate(expression.right);
	}

	/*
	 * visitWhileStatement() mirrors implementation of while in Java.
	 */
	@Override
	public Void visitWhileStatement(While statement) {
		while (truthy(evaluate(statement.condition))) {
			execute(statement.body);
		}
		return null;
	}

	/*
	 * visitCallExpression() gives arguments to an existing function in Luria. It
	 * returns the return value of the function called. The Function object is
	 * passed reference to this Interpreter and its arguments.
	 */
	@Override
	public Object visitCallExpression(Call expression) {
		Object called = evaluate(expression.called);
		List<Object> arguments = new ArrayList<>();
		for (Expression a : expression.arguments) {
			arguments.add(evaluate(a));
		}
	    if (!(called instanceof interpretation.Callable)) {       
	        throw new InterpreterError(expression.rightParenthesis,          
	            "Expected function call.");
	      }    
		interpretation.Callable function = (interpretation.Callable) called;
	    if (arguments.size() != function.arity()) {       
	        throw new InterpreterError(expression.rightParenthesis, "Expecting " +
	            function.arity() + " arguments.");                    
	      }   
		return function.call(this, arguments);
	}

	/*
	 * visitReturnStatement() throws a Return exception. See notes at Return,
	 * Function and visitCallExpression()
	 */
	@Override
	public Void visitReturnStatement(Return statement) {
		Object value = null;
		if (statement.value != null)
			value = evaluate(statement.value);
		throw new interpretation.Return(value);
	}

	/*
	 * visitArrayExpression() returns all values in an array.
	 */
	@Override
	public Object visitArrayExpression(Array expression) {
		List<Object> components = new ArrayList<>();
		if (expression.components != null) {
			for (Expression component : expression.components) {
				components.add(evaluate(component));
			}
		}
		return components;
	}

	/*
	 * visitIndexExpression() returns the value at a given array index.
	 */
	@Override
	public Object visitIndexExpression(Index expression) {
		Object object = evaluate(expression.object);
		if (!(object instanceof List)) {
			throw new InterpreterError(expression.symbol, "Error: array expected.");
		}
		List array = (List) object;
		Object objectIndex = evaluate(expression.index);
		if (!(objectIndex instanceof Double)) {
			throw new InterpreterError(expression.symbol, "Error: integer expected.");
		}
		int index = ((Double) objectIndex).intValue();
		if (index >= array.size()) {
			throw new InterpreterError(expression.symbol, "Error: index is beyond array range.");
		}
		return array.get(index);
	}

	/*
	 * visitVariableExpression() retrieves the value of a variable held in the
	 * MemoryEnvironment.
	 */
	@Override
	public Object visitVariableExpression(VariableExpression expression) {
		return environment.load(expression.symbol);
	}

	/*
	 * visitAssignmentExpression() extracts and evaluates the assignment
	 * expression's value and reassigns this to the given variable in the
	 * MemoryEnvironment.
	 */
	@Override
	public Object visitAssignmentExpression(Assignment expression) {
		Object value = evaluate(expression.value);
		environment.storeExisting(expression.symbol, value);
		return value;
	}

	/*
	 * visitPrintStatement() prints its child expression.
	 */
	@Override
	public Void visitPrintStatement(Print statement) {
		Object value = evaluate(statement.expression);
		System.out.println(stringify(value));
		return null;
	}
 
	/*
	 * visitReadBooleanStatement() is one of three read statement types. To overcome
	 * Luria's dynamic typing, distinct statements for reading Boolean values,
	 * numbers, and strings in Luria variables have been created, avoiding the need
	 * for a system of type checking. Each read statement applies a Java Scanner
	 * object to retrieve input from the user. Input is then parsed for the
	 * applicable terminal (literal) value, else an error is thrown. This value is
	 * then stored (assigned to) in the MemoryEnvironment.
	 */
	@Override
	public Void visitReadBooleanStatement(ReadBoolean statement) {
		Token t = ((Expression.VariableExpression) statement.expression).symbol;
		Scanner s = new Scanner(System.in);
		while (true) {
			String input = s.nextLine();
			if (input.equals("true")) {
				Object value = evaluate(new Expression.Literal(input));
				environment.storeExisting(t, value);
				break;
			} else if (input.equals("false")) {
				Object value = evaluate(new Expression.Literal(input));
				environment.storeExisting(t, value);
				break;
			} else {
				throw new InterpreterError(t, "Boolean value expected.");
			}
		}

		return null;
	}

	/*
	 * visitReadStringStatement().
	 */
	@Override
	public Void visitReadStringStatement(ReadString statement) {
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		Object value = evaluate(new Expression.Literal(input));
		Token t = ((Expression.VariableExpression) statement.expression).symbol;
		environment.storeExisting(t, value);
		return null;
	}

	/*
	 * visitReadNumberStatement().
	 */
	@Override
	public Void visitReadNumberStatement(ReadNumber statement) {
		Scanner s = new Scanner(System.in);
		double input = s.nextDouble();
		Object value = evaluate(new Expression.Literal(input));
		Token t = ((Expression.VariableExpression) statement.expression).symbol;
		environment.storeExisting(t, value);
		return null;
	}

	/*
	 * visitAllocationExpression() assigns an expression value to an array at a
	 * given index.
	 */
	@Override
	public Object visitAllocationExpression(Allocation expression) {
		Expression.Index subscript = null;
		if (expression.index instanceof Expression.Index) {
			subscript = (Expression.Index) expression.index;
		}
		Object listObject = evaluate(subscript.object);
		if (!(listObject instanceof List)) {
			throw new InterpreterError(expression.symbol, "Expected array.");
		}
		List<Object> list = (List) listObject;
		Object indexObject = evaluate(subscript.index);
		if (!(indexObject instanceof Double)) {
			throw new InterpreterError(expression.symbol, "Expected expression for array index.");
		}
		int index = ((Double) indexObject).intValue();
		if (index >= list.size()) {
			throw new InterpreterError(expression.symbol, "Array index out of range.");
		}
		Object value = evaluate(expression.value);
		list.set(index, value);
		return value;
	}

}
