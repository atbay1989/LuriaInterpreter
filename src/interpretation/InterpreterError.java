/*
 * The InterpreterError class extends Java RuntimeException. Because the interpreter traverses and evaluates nested statements
 * using recursive method calls, a means of escape (for the purposes of returning error data) to the class concerned with error
 * reporting (LuriaInterpreter), is required. This exception thrown up through the levels in the Java call stack and caught by
 * the Interpreter at interpret().
 * 
 * */

package interpretation;

import lexical_analysis.Token;

public class InterpreterError extends RuntimeException {	
	public final Token token;

	public InterpreterError(Token token, String message) {
		super(message);
		this.token = token;
	}
	
}
