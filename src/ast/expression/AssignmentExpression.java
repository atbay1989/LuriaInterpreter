package ast.expression;

import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class AssignmentExpression extends Expression {
	
	private Expression left;
	private Expression expression;
	
	public AssignmentExpression(Expression left, Expression expression) {
		this.left = left;
		this.expression = expression;
	}

	@Override
	public <E> E accept(ReturnVisitor<E> visitor) {
		return visitor.visit(this);	
	}

	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		visitor.visit(this);		
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "AssignmentExpression[" + left.toString() + " := " + expression.toString() + "]";
	}
		
}
