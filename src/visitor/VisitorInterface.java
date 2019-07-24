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

public interface VisitorInterface {
	
	// program
	public abstract Object visit(Program s, Object o);
	
	// statements
	public abstract Object visit(Statement s, Object o);
	public abstract Object visit(BlockStatement s, Object o);
	public abstract Object visit(PrintStatement s, Object o);
	public abstract Object visit(VariableDeclaration s, Object o);
	
	// expressions
	public abstract Object visit(ArithmeticBinaryExpression e, Object o);
	// change AssignmentExpression to statement?
	public abstract Object visit(AssignmentExpression e, Object o);
	public abstract Object visit(IntegerLiteral e, Object o);
	public abstract Object visit(Variable e, Object o);
	public abstract Object visit(VariableLiteral e, Object o);

}
