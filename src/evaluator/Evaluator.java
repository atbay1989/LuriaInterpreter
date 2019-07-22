package evaluator;

import ast.expression.ArithmeticBinaryExpression;
import ast.expression.AssignmentExpression;
import ast.expression.Expression;
import ast.expression.IntegerLiteral;
import ast.expression.Variable;
import ast.statement.BlockStatement;
import ast.statement.PrintStatement;
import ast.statement.Statement;
import environment.Environment;

public class Evaluator {
	
	public Environment environment;
	
	public Evaluator() {
		System.out.println("---EVALUATION---");
		environment = new Environment(this);
	}

	// interpret
	public void interpret(Statement statement) {		
		// BlockStatement
		if (statement instanceof BlockStatement) {
			for (Statement s : ((BlockStatement) statement).getStatements()) {
				interpretStatement(s);
			}
			
		// Statement
		} else {
			interpretStatement(statement);
		}
	}

	// interpretStatement
	private void interpretStatement(Statement statement) {
		// AssignmentExpression
		if (statement instanceof AssignmentExpression) {				
			Variable v = (Variable) ((AssignmentExpression) statement).getLeft();
			assign(v, ((AssignmentExpression) statement).getExpression());
		}	
		
		// PrintStatement
		if (statement instanceof PrintStatement) {
			System.out.println("---PRINT---");
			System.out.println(environment.lookup((Variable) ((PrintStatement) statement).getExpression()).getValue());
		}
	}

	// interpretExpression
	private int interpretExpression(Expression expression) {		
		
		// ArithmeticBinaryExpression
		if (expression instanceof ArithmeticBinaryExpression) {
			int left = evaluate(((ArithmeticBinaryExpression) expression).getLeft());
			int right = evaluate(((ArithmeticBinaryExpression) expression).getExpression());
			int result;
			switch (((ArithmeticBinaryExpression) expression).getOperation()) {			
				case ADDITION:
					result = left + right;
					System.out.println(left + " + " + right + " = " + result);
					return result;
				case SUBTRACTION:
					result = left - right;
					System.out.println(left + " - " + right + " = " + result);
					return result;
				case MULTIPLICATION:
					result = left * right;
					System.out.println(left + " * " + right + " = " + result);
					return result;
				case DIVISION:
					result = left / right;
					System.out.println(left + " / " + right + " = " + result);
					return result;
				case MODULO:
					result = left % right;
					System.out.println(left + " % " + right + " = " + result);
					return result;
				default:
					System.out.println("ERROR -> Operation not recognised.");
			}
		}
		
		// IntegerLiteral
		if (expression instanceof IntegerLiteral) {
			return evaluate(expression); 
		}
		
		// Variable
		if (expression instanceof Variable) {
			return evaluate(expression);
		}
		
		// Default
		return 0;
		
	} 

	// evaluate
	private int evaluate(Expression expression) {
		// IntegerLiteral
		if (expression instanceof IntegerLiteral) {
			return ((IntegerLiteral) expression).getValue();
		}

		// Variable
		if (expression instanceof Variable) {
			return environment.lookup((Variable) expression).getValue();
		}
		
		// Default
		return 0;		
	}
	
	// assign
	public void assign(Variable v, Expression e) {
		int result = interpretExpression(e);
		System.out.println(v.getSymbol() + " := " + result);
		environment.store(new Variable(v.getSymbol(), result, v.getIndex()));
	}
	
}
