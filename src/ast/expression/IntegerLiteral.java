package ast.expression;

import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class IntegerLiteral extends Expression {
	
	private int value;
	
	public IntegerLiteral(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public <E> E accept(ReturnVisitor<E> visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "" + value;
	}
	
	
	
}
