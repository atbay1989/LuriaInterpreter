package ast.statement;

import ast.Signifier;
import ast.expression.Expression;
import visitor.ReturnVisitor;
import visitor.VoidVisitor;

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

	@Override
	public <E> E accept(ReturnVisitor<E> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		visitor.visit(this);		
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
	
	
}
