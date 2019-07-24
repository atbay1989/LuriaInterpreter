package environment;

import java.util.HashMap;
import java.util.Map;

import syntacticanalysis.RuntimeError;
import syntacticanalysis.Token;

public class MemoryEnvironment {

	private final MemoryEnvironment enclosing;
	private final Map<String, Object> table = new HashMap<>();
	
	// constructors: first is base case, i.e. global; second constructs with reference to its enclosing scope
	public MemoryEnvironment() {
		enclosing = null;
	}
	
	public MemoryEnvironment(MemoryEnvironment enclosing) {
		this.enclosing = enclosing;
	}

	// get()
	public Object get(Token symbols) {
		if (table.containsKey(symbols.lexeme)) {
			return table.get(symbols.lexeme);
		}
		if (enclosing != null) return enclosing.get(symbols);

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
		if (enclosing != null) {
			enclosing.store(symbol, value);
			return;
		}
		throw new RuntimeError(symbol, "Error: Variable '" + symbol.lexeme + "' is undefined.");
	}
	
}
