/*
 * Statement is an abstract class from which extend those classes that represent statement nodes in the
 * abstract syntax tree. These concrete classes are nested within Statement only for the sake of ease of
 * coding. Similarly, Statement has within it a nested Visitor interface and an abstract, generic accept()
 * method that subclasses implement. This application of the Visitor pattern allows for the definition
 * of complex methods specific to each Statement subclass in a single, separate class, Interpreter,
 * which implements the Visitor interface.
 * 
 * Significance of each subclass' fields is elaborated upon in comments at the Parser and Interpreter classes
 * that construct and evaluate these objects, respectively. 
 * 
 * */

package syntactic_analysis;

import java.util.List;

import interpretation.Interpreter;
import lexical_analysis.Token;

public abstract class Statement {
	
	public interface Visitor<T> {
		T visitExpressionStatement(ExpressionStatement statement);
		T visitPrintStatement(Print statement);
		T visitVariableDeclarationStatement(VariableDeclaration statement);
		T visitBlockStatement(Block statement);
		T visitFunctionDeclarationStatement(FunctionDeclaration statement);
		T visitIfStatement(If statement);
		T visitWhileStatement(While statement);
		T visitReadNumberStatement(ReadNumber statement);
		T visitReadStringStatement(ReadString statement);
		T visitReadBooleanStatement(ReadBoolean statement);
		T visitReturnStatement(Return statement);
		
	}
	
	public abstract <T> T accept(Visitor<T> visitor);
	
	public static class ReadBoolean extends Statement {
		public final Expression expression;

		ReadBoolean(Expression e) {
			this.expression = e;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitReadBooleanStatement(this);
		}

	}

	public static class ReadNumber extends Statement {
		public final Expression expression;

		ReadNumber(Expression e) {
			this.expression = e;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitReadNumberStatement(this);
		}

	}
	
	public static class ReadString extends Statement {
		public final Expression expression;

		ReadString(Expression e) {
			this.expression = e;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitReadStringStatement(this);
		}

	}

	public static class Return extends Statement {
		public final Token symbol;
		public final Expression value;

		public Return(Token symbol, Expression value) {
			this.symbol = symbol;
			this.value = value;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitReturnStatement(this);
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

	public static class FunctionDeclaration extends Statement {
		public final Token symbol;
		public final List<Token> arguments;
		public final List<Statement> functionBlock;

		public FunctionDeclaration(Token symbol, List<Token> arguments, List<Statement> functionBlock) {
			this.symbol = symbol;
			this.arguments = arguments;
			this.functionBlock = functionBlock;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitFunctionDeclarationStatement(this);
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
	
	public static class VariableDeclaration extends Statement {
		public final Token symbol;
		public final Expression initialisation;

		VariableDeclaration(Token symbol, Expression initialisation) {
			this.symbol = symbol;
			this.initialisation = initialisation;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitVariableDeclarationStatement(this);
		}

	}
	
}
