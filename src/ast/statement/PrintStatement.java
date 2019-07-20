package ast.statement;

import ast.expression.Expression;
import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class PrintStatement extends Statement {

	private Expression expression;
	
	public PrintStatement(Expression expression) {
		this.expression = expression;
	}
	
	public Expression getExpression() {
		return expression;
	}

	@Override
	public <E> E accept(ReturnVisitor<E> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		visitor.visit(this);		
	}

	@Override
	public String toString() {
		return "PrintStatement[" + expression + "]";
	}

}
