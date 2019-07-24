package syntacticanalysis;

import java.util.List;

import evaluator.Interpreter;

public abstract class Statement {
	
	public interface Visitor<T> {
		T visitExpressionStatement(ExpressionStatement statement);
		T visitPrintStatement(Print statement);
		T visitVariableStatement(Variable statement);
		T visitBlockStatement(Block statement);
		T visitClassStatement(Class statement);
		T visitFunctionStatement(Function statement);
		T visitIfStatement(If statement);
		T visitWhileStatement(While statement);
		
	}

	public static class Class extends Statement {

		final Token symbol;
		final Expression.VariableExpression superClass;
		final List<Statement.Function> methods;
		
		public Class(Token symbol, Expression.VariableExpression superClass, List<Statement.Function> methods) {
			this.symbol = symbol;
			this.superClass = superClass;
			this.methods = methods;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitClassStatement(this);
		}

	}

	public static class Block extends Statement {
		
		public final List<Statement> statements;
		
		Block(List<Statement> statements) {
			this.statements = statements;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitBlockStatement(this);
		}
	}
	
	public static class ExpressionStatement extends Statement {
		
		public final Expression expression;
		
		ExpressionStatement(Expression e) {
			this.expression = e;
		}
		
	    public <T> T accept(Visitor<T> visitor) {
	        return visitor.visitExpressionStatement(this);
	      }
	    
	}

	public static class Function extends Statement {

		final Token symbol;
		final List<Token> parameters;
		final List<Statement> body;

		public Function(Token symbol, List<Token> parameters, List<Statement> body) {
			this.symbol = symbol;
			this.parameters = parameters;
			this.body = body;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitFunctionStatement(this);
		}

	}

	public static class Print extends Statement {
		
		public final Expression expression;
		
		Print(Expression e) {
			this.expression = e;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitPrintStatement(this);
		}
		
	}
	
	public static class If extends Statement {
		// fields
		public final Expression condition;
		public final Statement thenBranch;
		public final Statement elseBranch;

		If(Expression condition, Statement thenBranch, Statement elseBranch) {
			this.condition = condition;
			this.thenBranch = thenBranch;
			this.elseBranch = elseBranch;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitIfStatement(this);
		}

	}

	public static class While extends Statement {
		// fields
		public final Expression condition;
		public final Statement body;

		public While(Expression condition, Statement body) {
			this.condition = condition;
			this.body = body;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitWhileStatement(this);
		}

	}
	
	public static class Variable extends Statement {

		public final Token symbol;
		public final Expression initialisation;

		Variable(Token symbol, Expression initialisation) {
			this.symbol = symbol;
			this.initialisation = initialisation;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitVariableStatement(this);
		}

	}

	public abstract <T> T accept(Visitor<T> visitor);
	
}
