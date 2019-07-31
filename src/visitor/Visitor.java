package visitor;

import syntactic_analysis.Expression.Assignment;
import syntactic_analysis.Expression.Binary;
import syntactic_analysis.Expression.Grouping;
import syntactic_analysis.Expression.Literal;
import syntactic_analysis.Expression.Logical;
import syntactic_analysis.Expression.Unary;
import syntactic_analysis.Expression.VariableExpression;
import syntactic_analysis.Statement.Block;
import syntactic_analysis.Statement.ExpressionStatement;
import syntactic_analysis.Statement.Function;
import syntactic_analysis.Statement.If;
import syntactic_analysis.Statement.Print;
import syntactic_analysis.Statement.VariableDeclaration;
import syntactic_analysis.Statement.While;

public interface Visitor<T> {
	// statements
	T visitExpressionStatement(ExpressionStatement statement);
	T visitPrintStatement(Print statement);
	T visitVariableStatement(VariableDeclaration statement);
	T visitBlockStatement(Block statement);
	T visitClassStatement(Class statement);
	T visitFunctionStatement(Function statement);
	T visitIfStatement(If statement);
	T visitWhileStatement(While statement);
	
	// expressions
	T visitAssignmentExpression(Assignment expression);
	T visitBinaryExpression(Binary expression);
	T visitGroupingExpression(Grouping expression);
	T visitLiteralExpression(Literal expression);
	T visitUnaryExpression(Unary expression);
	T visitLogicalExpression(Logical expression);
    T visitVariableExpression(VariableExpression expression);
    
}
