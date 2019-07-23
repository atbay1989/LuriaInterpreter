package syntacticanalysis;

import evaluator.Interpreter;

public abstract class Statement {
	
	public interface Visitor<T> {
		T visitExpressionStatement(ExpressionStatement statement);
		T visitPrintStatement(Print statement);
		
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

	public static class Print extends Statement {
		
		public final Expression expression;
		
		Print(Expression e) {
			this.expression = e;
		}

		public <T> T accept(Visitor<T> visitor) {
			return visitor.visitPrintStatement(this);
		}
		
	}

	public abstract <T> T accept(Visitor<T> visitor);
	
}
