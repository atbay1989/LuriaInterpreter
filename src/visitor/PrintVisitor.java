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

public class PrintVisitor implements Visitor {

	@Override
	public Object visit(Program s, Object o) {
		System.out.println(s);
		return null;
	}

	@Override
	public Object visit(Statement s, Object o) {
		System.out.println(s);
		return null;
	}

	@Override
	public Object visit(BlockStatement s, Object o) {
		System.out.println(s);
		for (Statement x : s.getStatements()) {
			//this.visit(x, o);
			x.accept(this);

		}
		return null;
	}

	@Override
	public Object visit(PrintStatement s, Object o) {
		System.out.println(s);
		//this.visit(s.getExpression(), o);
		s.getExpression().accept(this);
		return null;
	}

	@Override
	public Object visit(VariableDeclaration s, Object o) {
		System.out.println(s);
		return null;
	}

	@Override
	public Object visit(ArithmeticBinaryExpression e, Object o) {
		System.out.println(e);
		e.getLeft().accept(this);
		e.getExpression().accept(this);

		//this.visit(e.getLeft(), o);
		//this.visit(e.getExpression(), o);

		return null;
	}

	@Override
	public Object visit(AssignmentExpression e, Object o) {
		System.out.println("AssignmentExpression");
		e.getLeft().accept(this);
		e.getExpression().accept(this);
		//this.visit(e.getLeft(), o);
		//this.visit(e.getExpression(), o);
		return null;
	}

	@Override
	public Object visit(IntegerLiteral e, Object o) {
		System.out.println("IntegerLiteral " + e);
		return null;
	}

	@Override
	public Object visit(Variable e, Object o) {
		System.out.println("Variable " + e);
		return null;
	}

	@Override
	public Object visit(VariableLiteral e, Object o) {
		System.out.println(e);
		return null;
	}

}
