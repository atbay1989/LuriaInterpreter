package ast.statement;

import java.util.Collection;

import visitor.ReturnVisitor;
import visitor.VoidVisitor;

public class BlockStatement extends Statement {

	private Statement[] statements;
	
	public BlockStatement(Collection<Statement> statements) {
		this.statements = statements.toArray(new Statement[statements.size()]);
	}

	@Override
	public <E> E accept(ReturnVisitor<E> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <E> void accept(VoidVisitor<E> visitor) {
		visitor.visit(this);		
	}

	public Statement[] getStatements() {
		return statements;
	}
	
}
