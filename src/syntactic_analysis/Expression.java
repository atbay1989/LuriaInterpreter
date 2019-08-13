/*
 * Expression is an abstract class from which extend those classes that represent expression nodes in the
 * abstract syntax tree. These concrete classes are nested within Expression only for the sake of ease of
 * coding. Similarly, Expression has within it a nested Visitor interface and an abstract, generic accept()
 * method that subclasses implement. This application of the Visitor pattern allows for the definition
 * of complex methods specific to each Expression subclass in a single, separate class, Interpreter,
 * which implements the Visitor interface.
 * 
 * Significance of each subclass' fields is elaborated upon in comments at the Parser and Interpreter classes
 * that construct and evaluate these objects, respectively. 
 * 
 * */

package syntactic_analysis;

import java.util.List;

import lexical_analysis.Token;

public abstract class Expression {

	public abstract <T> T accept(Visitor<T> visitor);
	
	public interface Visitor<T> {
		T visitAllocationExpression(Allocation expression);
		T visitArrayExpression(Array expression);
		T visitAssignmentExpression(Assignment expression);
		T visitBinaryExpression(Binary expression);
		T visitCallExpression(Call expression);
		T visitCombinationExpression(Combination expression);
		T visitIndexExpression(Index expression);
		T visitLiteralExpression(Literal expression);
		T visitLogicalExpression(Logical expression);
		T visitUnaryExpression(Unary expression);
		T visitVariableExpression(VariableExpression expression);

	}

    public static class Allocation extends Expression {
        public final Expression index;
        public final Token symbol;
        public final Expression value;
    	
    	Allocation(Expression index, Token symbol, Expression value) {
            this.index = index;
            this.symbol = symbol;
            this.value = value;
        }

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitAllocationExpression(this);
		}

    }
	
	public static class Array extends Expression {
		public final List<Expression> components;

		public Array(List<Expression> components) {
			this.components = components;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitArrayExpression(this);
		}

	}

	public static class Assignment extends Expression {
		public final Token symbol;
		public final Expression value;

		Assignment(Token symbol, Expression value) {
			this.symbol = symbol;
			this.value = value;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitAssignmentExpression(this);
		}

	}

	public static class Binary extends Expression {
		public final Expression left;
		public final Token operator;
		public final Expression right;

		public Binary(Expression left, Token operator, Expression right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitBinaryExpression(this);
		}

	}
	
	public static class Call extends Expression {
		public final Expression called;
		public final Token rightParenthesis;
		public final List<Expression> arguments;

		public Call(Expression called, Token rightParenthesis, List<Expression> arguments) {
			this.called = called;
			this.rightParenthesis = rightParenthesis;
			this.arguments = arguments;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitCallExpression(this);
		}

	}

	public static class Combination extends Expression {
		public final Expression expression;

		public Combination(Expression expression) {
			this.expression = expression;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitCombinationExpression(this);
		}

	}
	
	public static class Index extends Expression {
		public final Expression object;
		public final Token symbol;
		public final Expression index;

		public Index(Expression object, Token symbol, Expression index) {
			this.object = object;
			this.symbol = symbol;
			this.index = index;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitIndexExpression(this);
		}

	}


	public static class Literal extends Expression {
		public final Object value;

		public Literal(Object value) {
			this.value = value;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitLiteralExpression(this);
		}

	}
	
	public static class Logical extends Expression {
		public final Expression left;
		public final Token operator;
		public final Expression right;

		public Logical(Expression left, Token operator, Expression right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitLogicalExpression(this);
		}

	}

	public static class Unary extends Expression {
		public final Token operator;
		public final Expression right;

		public Unary(Token operator, Expression right) {
			this.operator = operator;
			this.right = right;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitUnaryExpression(this);
		}

	}

	public static class VariableExpression extends Expression {
		public final Token symbol;

		VariableExpression(Token symbol) {
			this.symbol = symbol;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitVariableExpression(this);
		}

	}

}
