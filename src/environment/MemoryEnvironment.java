package environment;

import java.util.HashMap;
import java.util.Map;

import syntacticanalysis.RuntimeError;
import syntacticanalysis.Token;

public class MemoryEnvironment {

	private final MemoryEnvironment enclosing;
	private final Map<String, Object> table = new HashMap<>();
	
	// constructors
	public MemoryEnvironment() {
		enclosing = null;
	}
	
	public MemoryEnvironment(MemoryEnvironment enclosing) {
		this.enclosing = enclosing;
	}

	// get()
	public Object get(Token s) {
		if (table.containsKey(s.lexeme)) {
			return table.get(s.lexeme);
		}
		if (enclosing != null) return enclosing.get(s);

		throw new RuntimeError(s, "Error: Undefined variable '" + s.lexeme + "'.");
	}

	public void lookup(String symbol, Object value) {
		table.put(symbol, value);
	}
	
	public void store(Token symbol, Object value) {
		if (table.containsKey(symbol.lexeme)) {
			table.put(symbol.lexeme, value);
			return;
		}
		if (enclosing != null) {
			enclosing.store(symbol, value);
			return;
		}
		
		throw new RuntimeError(symbol, "Error: Undefined variable '" + symbol.lexeme + "'.");
	}
	
}
