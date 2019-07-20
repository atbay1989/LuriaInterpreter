package ast.expression;

import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class Variable extends Expression {
	
	String symbol;
	int value;
	int index;
	
	public Variable(String symbol, int value, int index) {
		this.symbol = symbol;
		this.value = value;
		this.index = index;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getIndex() {
		return this.index;
	}
	
	public void setIndex(int index) {
		this.index = index;
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
		return symbol;
	}
	
}
