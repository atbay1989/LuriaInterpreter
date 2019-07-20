package ast.expression;

import visitor.Visitor;

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
	public String toString() {
		//return "ArithmeticBinaryExpression " + super.getLeft().toString() + " + " + super.getExpression().toString();
		return "ArithmeticBinaryExpression";
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

}


