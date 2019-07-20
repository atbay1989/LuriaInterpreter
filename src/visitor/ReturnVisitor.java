package visitor;

import ast.Program;
import ast.expression.ArithmeticBinaryExpression;
import ast.expression.AssignmentExpression;
import ast.expression.IntegerLiteral;
import ast.expression.VariableLiteral;
import ast.statement.BlockStatement;
import ast.statement.PrintStatement;
import ast.statement.VariableDeclaration;

public interface ReturnVisitor<E> {	
	E visit(Visitable e);
	
	E visit(Program e);
	
	E visit(BlockStatement e);
	E visit(PrintStatement e);
	
	E visit(VariableDeclaration e);
	
	E visit(ArithmeticBinaryExpression e);
	E visit(AssignmentExpression e);
	
	E visit(IntegerLiteral e);
	E visit(VariableLiteral e);
	
}
