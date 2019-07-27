package visitor;

import lexical_analysis.Token;
import lexical_analysis.TokenType;
import syntactic_analysis.Expression;
import syntactic_analysis.Expression.Assignment;
import syntactic_analysis.Expression.Binary;
import syntactic_analysis.Expression.Grouping;
import syntactic_analysis.Expression.Literal;
import syntactic_analysis.Expression.Logical;
import syntactic_analysis.Expression.Unary;
import syntactic_analysis.Expression.VariableExpression;

public class PrettyPrinter implements Expression.Visitor<String> {
	
	public String print(Expression e) {                                            
		    return e.accept(this);                                          
		  }     

	@Override
	public String visitBinaryExpression(Binary e) {
		return parenthesise(e.operator.lexeme, e.left, e.right);
	}

	@Override
	public String visitGroupingExpression(Grouping e) {
		return parenthesise("group", e.expression);
	}

	@Override
	public String visitLiteralExpression(Literal e) {
	    if (e.value == null) {
	    	return "nil";                            
	    }
	    return e.value.toString();
	}

	@Override
	public String visitUnaryExpression(Unary e) {
	    return parenthesise(e.operator.lexeme, e.right);           

	}

	private String parenthesise(String name, Expression... expressions) {
		StringBuilder builder = new StringBuilder();
		builder.append("(").append(name);
		for (Expression e : expressions) {
			builder.append(" ");
			builder.append(e.accept(this));
		}
		builder.append(")");

		return builder.toString();
	} 
	
	public static void main(String[] args) {
		
		Expression expression = new Expression.Binary(
				new Expression.Unary(new Token(TokenType.MINUS, "-", null, 1), new Expression.Literal(7)),
				new Token(TokenType.ASTERISK, "*", null, 1), new Expression.Grouping(new Expression.Literal(5)));

		System.out.println(new PrettyPrinter().print(expression));
	}

	@Override
	public String visitAssignmentExpression(Assignment expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitVariableExpression(VariableExpression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitLogicalExpression(Logical expression) {
		// TODO Auto-generated method stub
		return null;
	}
}
