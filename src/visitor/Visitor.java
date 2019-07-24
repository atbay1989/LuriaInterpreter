package visitor;

import syntacticanalysis.Statement.Block;
import syntacticanalysis.Statement.Class;
import syntacticanalysis.Statement.ExpressionStatement;
import syntacticanalysis.Statement.Function;
import syntacticanalysis.Statement.If;
import syntacticanalysis.Statement.Print;
import syntacticanalysis.Statement.Variable;
import syntacticanalysis.Statement.While;

import syntacticanalysis.Expression.Assignment;
import syntacticanalysis.Expression.Binary;
import syntacticanalysis.Expression.Grouping;
import syntacticanalysis.Expression.Literal;
import syntacticanalysis.Expression.Logical;
import syntacticanalysis.Expression.Unary;
import syntacticanalysis.Expression.VariableExpression;

public interface Visitor<T> {
	// statements
	T visitExpressionStatement(ExpressionStatement statement);
	T visitPrintStatement(Print statement);
	T visitVariableStatement(Variable statement);
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
