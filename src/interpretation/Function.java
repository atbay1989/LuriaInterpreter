/*
 * The Function class implements the Callable interface method call(). Given a reference to the 'calling'
 * Interpreter, the Function call() method instantiates a new MemoryEnvironment. This provides each called
 * function in Luria with its own MemoryEnvironment within which the variables declared within the function
 * block or parameters passed to a function are stored. Variables belonging to the function's outer blocks 
 * remain accessible, but further functions called within a function block (i.e. recursively) become inaccessible.
 * 
 * Functions in Luria return null except when a ReturnStatement is encountered upon evaluation of the function
 * block. See also comment at Return. 
 * 
 * */

package interpretation;

import java.util.List;

import syntactic_analysis.Statement;

public class Function implements Callable {
	private final Statement.FunctionDeclaration declaration;

	public Function(Statement.FunctionDeclaration declaration) {
		this.declaration = declaration;
	}

	@Override
	public Object call(Interpreter interpreter, List<Object> arguments) {
		MemoryEnvironment functionEnvironment = new MemoryEnvironment(interpreter.globalScope);
		for (int i = 0; i < declaration.arguments.size(); i++) {
			functionEnvironment.store(declaration.arguments.get(i).lexeme, arguments.get(i));
		}
		try {
			interpreter.executeBlock(declaration.functionBlock, functionEnvironment);
		} catch (Return returnValue) {
			return returnValue.value;
		}
		interpreter.executeBlock(declaration.functionBlock, functionEnvironment);
		return null;
	}

	@Override
	public String toString() {
		return "<function " + declaration.symbol.lexeme + "";
	}

}
