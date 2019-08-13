/*
 * The MemoryEnvironment class represents the 'environment' or the data structure responsible for storing
 * and retrieving variable values; this feature of the interpreter program affords the Luria language with
 * the quality of being 'stateful', i.e. preceding events, such as variable or function declarations, can be
 * remembered by the interpreter program. Every instance of MemoryEnviornment has in its fields a reference 
 * to the MemoryEnvironment belonging to the Interpreter instance that instantiated it, except in the base
 * case (i.e. 'global scope') where 'outer' = null. This instantiation occurs upon the Interpreter class' 
 * visiting of a blockStatement, such as that composing a function declaration statement. Consequently, in a
 * given state, the interpreter program has access recursively to those variables or functions declared in its
 * own environment and all those that enclose it.
 * 
 * */

package interpretation;

import java.util.HashMap;
import java.util.Map;

import lexical_analysis.Token;

public class MemoryEnvironment {
	private final MemoryEnvironment outer;
	private final Map<String, Object> table = new HashMap<>();

	public MemoryEnvironment() {
		outer = null;
	}

	public MemoryEnvironment(MemoryEnvironment outer) {
		this.outer = outer;
	}

	/*
	 * load() retrieves from the MemoryEnvironment HashMap the value of a variable
	 * by way of its key, i.e. symbol. If this MemoryEnvironment is not 'global'
	 * load() is called recursively to check for variables of the given key existing
	 * in those environments that enclose it.
	 */
	public Object load(Token symbols) {
		if (table.containsKey(symbols.lexeme)) {
			return table.get(symbols.lexeme);
		}
		if (outer != null)
			return outer.load(symbols);

		throw new InterpreterError(symbols, "Variable '" + symbols.lexeme + "' is undefined.");
	}

	/*
	 * store() binds a symbol or name to a value, storing it in the
	 * MemoryEnvironment HashMap. Because store() does not check for an existing
	 * variable associated with the given symbol, variables can be redeclared. This
	 * method is called from routines that declare or redeclare variables.
	 */
	public void store(String symbol, Object value) {
		table.put(symbol, value);
	}

	/*
	 * storeExisting() checks for an existing variables of a given key in this
	 * MemoryEnvironment HashMap or in that of its outer MemoryEnvironment. This is
	 * the method called from routines that assign values to existing variables.
	 */
	public void storeExisting(Token symbol, Object value) {
		if (table.containsKey(symbol.lexeme)) {
			table.put(symbol.lexeme, value);
			return;
		}
		if (outer != null) {
			outer.storeExisting(symbol, value);
			return;
		}
		throw new InterpreterError(symbol, "Variable '" + symbol.lexeme + "' is undefined.");
	}

}
