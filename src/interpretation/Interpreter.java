/*
 * The Function class implements the Callable interface method call(). Given a reference to the 'calling'
 * Interpreter, the Function call() method instantiates a new MemoryEnvironment. This provides each called
 * function in Luria with its own MemoryEnvironment within which the variables declared within the function
 * block or parameters passed to a function are stored. Variables belonging to the function's outer blocks 
 * remain accessible, but further functions called within a function block (i.e. recursively) become inaccessible.
 * 
 * Functions in Luria return null except when a ReturnStatement is encountered upon evaluation of the function
 * block. See also comment at Return. 
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
import syntactic_analysis.Expression.Grouping;
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

	public void interpret(List<Statement> statements) {
		try {
			for (Statement s : statements) {
				execute(s);
			}
		} catch (InterpreterError error) {
			LuriaInterpreter.interpreterError(error);
		}
	}

	private Object evaluate(Expression e) {
		return e.accept(this);
	}

	private void execute(Statement s) {
		s.accept(this);
	}

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

	private void checkOperand(Token operator, Object operand) {
		if (operand instanceof Double)
			return;
		throw new InterpreterError(operator, "Error: Operand must be a number.");
	}

	private void checkOperands(Token operator, Object left, Object right) {
		if (left instanceof Double && right instanceof Double)
			return;
		throw new InterpreterError(operator, "Error: Operands must be numbers.");
	}

	private boolean truthy(Object o) {
		if (o == null)
			return false;
		if (o instanceof Boolean)
			return (boolean) o;
		return true;
	}

	private boolean equal(Object l, Object r) {
		if (l == null && r == null)
			return true;
		if (l == null)
			return false;

		return l.equals(r);
	}

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

	@Override
	public Object visitBinaryExpression(Binary e) {
		Object left = evaluate(e.left);
		Object right = evaluate(e.right);

		switch (e.operator.type) {
		// arithmetic
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
		// comparison operators
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
		// equality
		case EXCLAMATION_EQUAL:
			return !equal(left, right);
		case EQUAL_EQUAL:
			return equal(left, right);
		}
		return null;
	}

	@Override
	public Object visitGroupingExpression(Grouping e) {
		return evaluate(e.expression);
	}

	@Override
	public Object visitLiteralExpression(Literal e) {
		return e.value;
	}

	@Override
	public Object visitUnaryExpression(Unary e) {
		// evaluate the operand expression; apply the unary operator itself to result
		Object right = evaluate(e.right);
		switch (e.operator.type) {
		// - negates result of subexpression
		case MINUS:
			checkOperand(e.operator, right);
			return -(double) right;
		case EXCLAMATION:
			return !truthy(right);
		}
		return null;
	}

	@Override
	public Void visitExpressionStatement(Statement.ExpressionStatement statement) {
		evaluate(statement.expression);
		return null;
	}

	@Override
	public Void visitVariableDeclarationStatement(VariableDeclaration statement) {
		Object value = null;
		if (statement.initialisation != null) {
			value = evaluate(statement.initialisation);
		}
		environment.store(statement.symbol.lexeme, value);
		return null;
	}

	@Override
	public Void visitBlockStatement(Block statement) {
		executeBlock(statement.statements, new MemoryEnvironment(environment));
		return null;
	}

	@Override
	public Void visitFunctionDeclarationStatement(FunctionDeclaration statement) {
		interpretation.Function function = new interpretation.Function(statement);
		environment.store(statement.symbol.lexeme, function);
		return null;
	}

	@Override
	public Void visitIfStatement(If statement) {
		if (truthy(evaluate(statement.condition))) {
			execute(statement.thenBranch);
		} else if (statement.elseBranch != null) {
			execute(statement.elseBranch);
		}
		return null;
	}

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

	@Override
	public Void visitWhileStatement(While statement) {
		while (truthy(evaluate(statement.condition))) {
			execute(statement.body);
		}
		return null;
	}

/*  visitCallExpression() upon call of the Function class' call() method, to which is passed a reference to the current
 *  MemoryEnvironment and the list of arguments (i.e. parameters) associated with the function...
 *  */
	@Override
	public Object visitCallExpression(Call expression) {
		Object called = evaluate(expression.called);
		List<Object> arguments = new ArrayList<>();
		for (Expression a : expression.arguments) {
			arguments.add(evaluate(a));
		}
		interpretation.Callable function = (interpretation.Callable) called;
		return function.call(this, arguments);
	}

	@Override
	public Void visitReturnStatement(Return statement) {
		Object value = null;
		if (statement.value != null)
			value = evaluate(statement.value);
		throw new interpretation.Return(value);
	}

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

	@Override
	public Object visitIndexExpression(Index expression) {
        Object object = evaluate(expression.object);
        if (!(object instanceof List)) {
            throw new InterpreterError(expression.symbol, "Error: array expected.");
        }
        List array = (List)object;
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
	
	@Override
	public Object visitVariableExpression(VariableExpression expression) {
		return environment.load(expression.symbol);
	}

	@Override
	public Object visitAssignmentExpression(Assignment expression) {
		Object value = evaluate(expression.value);
		environment.storeExisting(expression.symbol, value);
		return value;
	}
	
	@Override
	public Void visitPrintStatement(Print statement) {
		Object value = evaluate(statement.expression);
		System.out.println(stringify(value));
		return null;
	}

	@Override
	public Void visitReadStringStatement(ReadString statement) {
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		Object value = evaluate(new Expression.Literal(input));
		Token t = ((Expression.VariableExpression) statement.expression).symbol;
		environment.storeExisting(t, value);
		return null;
	}

	@Override
	public Void visitReadNumberStatement(ReadNumber statement) {
		Scanner s = new Scanner(System.in);
		double input = s.nextDouble();
		Object value = evaluate(new Expression.Literal(input));
		Token t = ((Expression.VariableExpression) statement.expression).symbol;
		environment.storeExisting(t, value);
		return null;
	}

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

	@Override
	public Object visitAllocationExpression(Allocation expression) {
		Expression.Index subscript = null;
        if (expression.object instanceof Expression.Index) {
            subscript = (Expression.Index)expression.object;
        }
        Object listObject = evaluate(subscript.object);
        if (!(listObject instanceof List)) {
            throw new InterpreterError(expression.symbol, "");
        }
        List<Object> list = (List)listObject;
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
