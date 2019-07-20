package ast.expression;

import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class ArithmeticBinaryExpression extends BinaryExpression {
	
	//private Operation operation;
	
/*	public ArithmeticBinaryExpression(Operation operation, Expression left, Expression expression) {
		super(left, expression);
		this.operation = operation;
	}*/
	
	public ArithmeticBinaryExpression(Expression left, Expression expression) {
		super(left, expression);
	}
	
	// enum nested class START
	
	public static enum Operation {
		ADDITION("+"), SUBTRACTION("-");
		
		private String symbol;
		
		Operation(String symbol) {
			this.symbol = symbol;
		}
		
	}
	
	// enum nested class END
	
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
		return "ArithmeticBinaryExpression " + super.getLeft().toString() + " + " + super.getExpression().toString();
	}

}


