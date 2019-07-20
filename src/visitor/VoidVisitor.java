package visitor;

import ast.Program;
import ast.expression.ArithmeticBinaryExpression;
import ast.expression.AssignmentExpression;
import ast.expression.IntegerLiteral;
import ast.expression.Variable;
import ast.expression.VariableLiteral;
import ast.statement.BlockStatement;
import ast.statement.PrintStatement;
import ast.statement.VariableDeclaration;

public interface VoidVisitor<E> {
	void visit(Visitable e);
	
	void visit(Program e);
	
	void visit(BlockStatement e);
	void visit(PrintStatement e);
	
	void visit(VariableDeclaration e);
	
	void visit(ArithmeticBinaryExpression e);
	void visit(AssignmentExpression e);
	
	void visit(IntegerLiteral e);
	void visit(VariableLiteral e);
	void visit(Variable e);

}
