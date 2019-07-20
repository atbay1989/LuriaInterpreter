package ast.expression;

public abstract class BinaryExpression extends Expression {

	private Expression left;
	private Expression expression;
	
	public BinaryExpression(Expression left, Expression expression) {
		this.left = left;
		this.expression = expression;
	}

	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
}
