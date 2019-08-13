/*
 * The Return object extends the Java RuntimeException. The purpose of the custom Return exception is to provide to 
 * Luria the capacity to return a value from within a block, e.g. from a function. The exception is thrown when the
 * Interpreter class visits a ReturnStatement node and is caught in the Function class at call() that, in turn, returns
 * the value to the Interpreter at visitCallExpression().
 * 
 * */

package interpretation;

public class Return extends RuntimeException {
	public final Object value;
	
	public Return(Object value) {
		this.value = value;
	}

}
