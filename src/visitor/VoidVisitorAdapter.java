package visitor;

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

public class VoidVisitorAdapter<E> implements VoidVisitor<E> {

	@Override
	public void visit(Visitable e) {}

	@Override
	public void visit(Program e) {}

	@Override
	public void visit(BlockStatement e) {
		for(Statement statement : e.getStatements()) {
			statement.accept(this);
		}		
	}

	@Override
	public void visit(PrintStatement e) {
		e.getExpression().accept(this);
	}

	@Override
	public void visit(VariableDeclaration e) {
		if (e.getInitialisation() != null) {
			e.getInitialisation().accept(this);
		}
	}

	@Override
	public void visit(ArithmeticBinaryExpression e) {
		e.getLeft().accept(this);
		e.getExpression().accept(this);		
	}

	@Override
	public void visit(AssignmentExpression e) {
		e.getLeft().accept(this);
		e.getExpression().accept(this);		
	}

	@Override
	public void visit(IntegerLiteral e) {}

	@Override
	public void visit(VariableLiteral e) {}

	@Override
	public void visit(Variable e) {}

}
