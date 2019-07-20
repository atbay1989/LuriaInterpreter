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
		environment = new Environment(this);
	}

	// interpret
	public void interpret(Statement statement) {		
		// BlockStatement
		if (statement instanceof BlockStatement) {
			for (Statement s : ((BlockStatement) statement).getStatements()) {
				//System.out.println(s.toString());
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
			System.out.println(environment.lookup((Variable) ((PrintStatement) statement).getExpression()).getValue());
		}
	}

	// interpretExpression
	private int interpretExpression(Expression expression) {		
		// ArithmeticBinaryExpression (i.e. ADDITION)
		// Note: Type might allow to switch between ADDITION, SUBTRACTION, etc.
		if (expression instanceof ArithmeticBinaryExpression) {
			int result = evaluate(((ArithmeticBinaryExpression) expression).getLeft()) +
					evaluate(((ArithmeticBinaryExpression) expression).getExpression());
			System.out.println("SUM " + result);
			return result;
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
		System.out.println("ASSIGN " + result + " TO " + v.getSymbol());
		environment.store(new Variable(v.getSymbol(), result, v.getIndex()));
	}
	
}
