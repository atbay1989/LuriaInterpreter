package syntactic_analysis;

import lexical_analysis.Token;

public abstract class Expression {

	public interface Visitor<T> {
		T visitAssignmentExpression(Assignment expression);
		T visitBinaryExpression(Binary expression);
		T visitGroupingExpression(Grouping expression);
		T visitLiteralExpression(Literal expression);
		T visitUnaryExpression(Unary expression);
		T visitLogicalExpression(Logical expression);
	    T visitVariableExpression(VariableExpression expression);
		
	}

	public static class Assignment extends Expression {
		// field
		public final Token symbol;
		public final Expression value;
		
		// constructor
		Assignment(Token symbol, Expression value) {
			this.symbol = symbol;
			this.value = value;
		}

		// visitor
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitAssignmentExpression(this);
		}

	}

	public static class Binary extends Expression {
		// fields
		public final Expression left;
		public final Token operator;
		public final Expression right;
		
		// constructor
		public Binary(Expression left, Token operator, Expression right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		// visitor
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitBinaryExpression(this);
		}

	}

	public static class Grouping extends Expression {
		// fields
		public final Expression expression;
		
		// constructor
		public Grouping(Expression expression) {
			this.expression = expression;
		}

		// visitor
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitGroupingExpression(this);
		}

	}

	public static class Literal extends Expression {
		// fields
		public final Object value;

		// constructor
		public Literal(Object value) {
			this.value = value;
		}

		// visitor
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitLiteralExpression(this);
		}

	}

	public static class Unary extends Expression {
		// fields
		public final Token operator;
		public final Expression right;
		
		// constructor
		public Unary(Token operator, Expression right) {
			this.operator = operator;
			this.right = right;
		}

		// visitor
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitUnaryExpression(this);
		}

	}
	
	public static class Logical extends Expression {
	    // fields
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

	public static class VariableExpression extends Expression {
		// fields
		public final Token symbol;
		
		// constructor
		VariableExpression(Token symbol) {
			this.symbol = symbol;
		}

		// visitor
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitVariableExpression(this);
		}

	}
	
	// abstract visitor
	public abstract <T> T accept(Visitor<T> visitor);
}
