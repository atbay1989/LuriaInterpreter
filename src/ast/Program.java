package ast;

import visitor.Visitor;

public class Program extends ASTNode {

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

}
