package testsuite;

import java.util.ArrayList;

import ast.expression.ArithmeticBinaryExpression;
import ast.expression.AssignmentExpression;
import ast.expression.IntegerLiteral;
import ast.expression.Variable;
import ast.statement.BlockStatement;
import ast.statement.PrintStatement;
import ast.statement.Statement;
import evaluator.Evaluation;
import evaluator.Evaluator;

public class Test {

	public static void main(String[] args) {	
		
		// program block
		ArrayList<Statement> statements = new ArrayList<Statement>();
		
		// x := 2
		Variable x = new Variable("x", 0, 0);
		IntegerLiteral il1 = new IntegerLiteral(2);
		AssignmentExpression ae1 = new AssignmentExpression(x, il1);
		
		// y := (x + 3)
		Variable y = new Variable("y", 0, 1);
		IntegerLiteral il2 = new IntegerLiteral(3);
		ArithmeticBinaryExpression abe1 = new ArithmeticBinaryExpression(x, il2);
		AssignmentExpression ae2 = new AssignmentExpression(y, abe1);
		
		// z := (x + y)
		Variable z = new Variable("z", 0, 2);
		ArithmeticBinaryExpression abe2 = new ArithmeticBinaryExpression(x, y);
		AssignmentExpression ae3 = new AssignmentExpression(z, abe2);
		
		// print(z)
		PrintStatement ps = new PrintStatement(z);
		 
		statements.add(ae1);	
		statements.add(ae2);		
		statements.add(ae3);		
		statements.add(ps);		

		BlockStatement b = new BlockStatement(statements);
		
		Evaluation e = new Evaluation();
		e.interpret(b); 
		e.environment.getTable();		
		
		Evaluator ev = new Evaluator();
		ev.visit(b);
	}
	
}
