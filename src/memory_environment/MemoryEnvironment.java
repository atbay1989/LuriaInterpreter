package memory_environment;

import java.util.HashMap;
import java.util.Map;

import lexical_analysis.Token;
import syntactic_analysis.RuntimeError;

public class MemoryEnvironment {

	private final MemoryEnvironment outer;
	private final Map<String, Object> table = new HashMap<>();
	
	// constructors: first is base case, i.e. global; second constructs with reference to its enclosing scope
	public MemoryEnvironment() {
		outer = null;
	}
	
	public MemoryEnvironment(MemoryEnvironment outer) {
		this.outer = outer;
	} 

	// get()
	public Object get(Token symbols) {
		if (table.containsKey(symbols.lexeme)) {
			return table.get(symbols.lexeme);
		}
		if (outer != null) return outer.get(symbols);

		throw new RuntimeError(symbols, "Error: Variable '" + symbols.lexeme + "' is undefined.");
	} 

	// loopup()
	public void lookup(String symbol, Object value) {
		table.put(symbol, value);
	}
	
	// store()
	public void store(Token symbol, Object value) {
		if (table.containsKey(symbol.lexeme)) {
			table.put(symbol.lexeme, value);
			return;
		}
		if (outer != null) {
			outer.store(symbol, value);
			return;
		}
		throw new RuntimeError(symbol, "Error: Variable '" + symbol.lexeme + "' is undefined.");
	}
	
}
