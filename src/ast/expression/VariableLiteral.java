package ast.expression;

import ast.Signifier;
import visitor.VisitorInterface;

public class VariableLiteral extends Expression {

	private Signifier signifier;
	
	public VariableLiteral(Signifier signifier) {
		this.signifier = signifier;
	}
	
	
	public Signifier getSignifier() {
		return signifier;
	}
	
	@Override
	public Object accept(VisitorInterface visitor) {
		return visitor.visit(this, null);
	}
	
}
