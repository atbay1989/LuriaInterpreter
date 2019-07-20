package ast.statement;

import java.util.Collection;

import visitor.Visitor;

public class BlockStatement extends Statement {

	private Statement[] statements;
	
	public BlockStatement(Collection<Statement> statements) {
		this.statements = statements.toArray(new Statement[statements.size()]);
	}

	public Statement[] getStatements() {
		return statements;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this, null);
	}

	@Override
	public String toString() {
		return "BlockStatement";
	}
	
}
