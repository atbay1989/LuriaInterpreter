package ast.expression;

import visitor.Visitor;

public class AssignmentExpression extends Expression {
	
	private Expression left;
	private Expression expression;
	
	public AssignmentExpression(Expression left, Expression expression) {
		this.left = left;
		this.expression = expression;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		//return "AssignmentExpression[" + left.toString() + " := " + expression.toString() + "]";
		return "AssignmentExpression";
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}
		
}
