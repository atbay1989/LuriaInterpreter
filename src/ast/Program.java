package ast;

import ast.statement.Statement;
import visitor.Visitor;

public class Program extends ASTNode {

	public Statement s;
	
	public Program(Statement s) {
		this.s = s;
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

}
