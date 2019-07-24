package ast;

import ast.statement.Statement;
import visitor.VisitorInterface;

public class Program extends ASTNode {

	public Statement s;
	
	public Program(Statement s) {
		this.s = s;
	}
	
	@Override
	public Object accept(VisitorInterface visitor) {
		return visitor.visit(this, null);
	}

}
