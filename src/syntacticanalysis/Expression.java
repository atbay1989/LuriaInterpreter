package syntacticanalysis;

import java.util.List;

public abstract class Expression {

	public interface Visitor<T> {
		T visitBinaryExpression(Binary expression);

		T visitGroupingExpression(Grouping expression);

		T visitLiteralExpression(Literal expression);

		T visitUnaryExpression(Unary expression);
	}

	public static class Binary extends Expression {

		public Binary(Expression left, Token operator, Expression right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitBinaryExpression(this);
		}

		public final Expression left;
		public final Token operator;
		public final Expression right;

	}

	public static class Grouping extends Expression {
		public Grouping(Expression expression) {
			this.expression = expression;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitGroupingExpression(this);
		}

		public final Expression expression;
	}

	public static class Literal extends Expression {
		public Literal(Object value) {
			this.value = value;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitLiteralExpression(this);
		}

		public final Object value;
	}

	public static class Unary extends Expression {
		public Unary(Token operator, Expression right) {
			this.operator = operator;
			this.right = right;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitUnaryExpression(this);
		}

		public final Token operator;
		public final Expression right;
	}

	public abstract <T> T accept(Visitor<T> visitor);
}
