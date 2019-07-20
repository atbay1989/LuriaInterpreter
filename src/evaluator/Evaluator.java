package evaluator;

import ast.Program;
import ast.expression.ArithmeticBinaryExpression;
import ast.expression.AssignmentExpression;
import ast.expression.IntegerLiteral;
import ast.expression.Variable;
import ast.expression.VariableLiteral;
import ast.statement.BlockStatement;
import ast.statement.PrintStatement;
import ast.statement.Statement;
import ast.statement.VariableDeclaration;
import environment.Environment;
import visitor.Visitable;
import visitor.VoidVisitor;

public class Evaluator implements VoidVisitor {

	protected Environment environment;
	
	public void Interpret(Statement s) {
		
		if (s instanceof AssignmentExpression) {
			
		}
	}
	
	@Override
	public void visit(Visitable e) {}

	@Override
	public void visit(Program e) {}

	@Override
	public void visit(BlockStatement e) {
		for (Statement statement : e.getStatements()) {
			statement.accept(this);
		}	
	}

	@Override
	public void visit(PrintStatement e) {
		System.out.println(e.toString());
		e.getExpression().accept(this);
		System.out.println(e.getExpression().toString());	
	}

	@Override
	public void visit(VariableDeclaration e) {}

	@Override
	public void visit(ArithmeticBinaryExpression e) {
		System.out.println(e.toString());

		e.getLeft().accept(this);
		e.getExpression().accept(this);		
	}

	@Override
	public void visit(AssignmentExpression e) {
		System.out.println(e.toString());
		e.getLeft().accept(this);
		e.getExpression().accept(this);	
	}

	@Override
	public void visit(IntegerLiteral e) {
		System.out.println("VISITED");
		System.out.println(e.toString());
		e.accept(this);
		System.out.println("IntegerLiteral");
		System.out.print(e.toString());
		
	}

	@Override
	public void visit(VariableLiteral e) {}

	@Override
	public void visit(Variable e) {
		System.out.println(e.getSymbol());
		e.accept(this);
		
	}

}
