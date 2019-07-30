package function;

import java.util.List;

import environment.MemoryEnvironment;
import evaluator.Interpreter;
import syntactic_analysis.Callable;
import syntactic_analysis.Statement;

public class Function implements Callable {
	private final Statement.Function declaration;
	
	public Function(Statement.Function declaration) {
		this.declaration = declaration;
	}

	@Override
	public Object call(Interpreter interpreter, List<Object> arguments) {
		MemoryEnvironment environment = new MemoryEnvironment(interpreter.global);
		for (int i = 0; i < declaration.arguments.size(); i++) {
			environment.lookup(declaration.arguments.get(i).lexeme, arguments.get(i));
		}
		interpreter.executeBlock(declaration.functionBlock, environment);
		return null;
	}
	
}
