/*
 * At present only instances of the Function class implement the Callable interface. See comment at Function.
 * 
 * */

package interpretation;

import java.util.List;

public interface Callable {
	Object call(Interpreter interpreter, List<Object> arguments);
	
}
