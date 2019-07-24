package ast.statement;

import ast.expression.Expression;
import visitor.VisitorInterface;

public class PrintStatement extends Statement {

	private Expression expression;
	
	public PrintStatement(Expression expression) {
		this.expression = expression;
	}
	
	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "PrintStatement[" + expression + "]";
	}

	@Override
	public Object accept(VisitorInterface visitor) {
		return visitor.visit(this, null);
	}

}
