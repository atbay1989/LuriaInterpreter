package ast.expression;

import visitor.Visitor;

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
	public String toString() {
		return symbol;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}
	
}
