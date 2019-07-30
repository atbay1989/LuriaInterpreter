package syntactic_analysis;

import java.util.List;

import evaluator.Interpreter;

public interface Callable {
	Object call(Interpreter interpreter, List<Object> arguments);
	
}
