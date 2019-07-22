package ast.expression;

import visitor.Visitor;

public class ArithmeticBinaryExpression extends BinaryExpression {
	
	private Operation operation;
	
	public ArithmeticBinaryExpression(Operation o, Expression left, Expression expression) {
		super(left, expression);
		this.operation = o;
		
	}
	
	// enum nested class START
	
	public static enum Operation {
		ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULO;
		
	}
	
	// enum nested class END

	public Operation getOperation() {
		return operation;
	}
	
	@Override
	public String toString() {
		return this.operation.toString();
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

}


