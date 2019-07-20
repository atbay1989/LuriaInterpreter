package ast.expression;

import ast.Signifier;
import visitor.ReturnVisitor;
import visitor.VoidVisitor;

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
	public <E> E accept(ReturnVisitor<E> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		visitor.visit(this);		
	}
	
}
