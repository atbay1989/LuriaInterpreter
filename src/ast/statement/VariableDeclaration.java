package ast.statement;

import ast.Signifier;
import ast.expression.Expression;
import visitor.Visitor;

public class VariableDeclaration extends Statement {

	//private Signifier signifier;
	private String signifier;
	private Expression initialisation;
	
	//public VariableDeclaration(Signifier signifier, Expression initialisation) {
	//	this.signifier = signifier;
	//	this.initialisation = initialisation;
	//}
	
	public VariableDeclaration(String signifier, Expression initialisation) {
		this.signifier = signifier;
		this.initialisation = initialisation;
	}

	//public Signifier getSignifier() {
	//	return signifier;
	//}
	
	public String getSignifier() {
		return signifier;
	}

	public Expression getInitialisation() {
		return initialisation;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

}
