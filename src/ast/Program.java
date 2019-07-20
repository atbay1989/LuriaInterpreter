package ast;

import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class Program extends ASTNode {

	@Override
	public <E> E accept(ReturnVisitor<E> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		visitor.visit(this);		
	}

}
