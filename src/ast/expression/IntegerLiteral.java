package ast.expression;

import visitor.Visitor;

public class IntegerLiteral extends Expression {
	
	private int value;
	
	public IntegerLiteral(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

}
