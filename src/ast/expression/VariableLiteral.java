package ast.expression;

import ast.Signifier;
import visitor.Visitor;

public class VariableLiteral extends Expression {

	//private Signifier signifier;
	private String signifier;
	
	//public VariableLiteral(Signifier signifier) {
	//	this.signifier = signifier;
	//}
	
	public VariableLiteral(String signifier) {
		this.signifier = signifier;
	}
	
	//public Signifier getSignifier() {
	//	return signifier;
	//}
	
	public String getSignifier() {
		return signifier;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}
	
}
