/*
 * The LuriaInterpreter class is the site of the main() and is responsible for receiving source code. Source code can be given
 * to the program read from a .txt file or given as single-line instructions by way of prompt. The concern of reporting errors
 * is handled here, separating it from the interpreter components, i.e. the lexer and parser, that generate those errors.
 * 
 * */

package luria_interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import interpretation.Interpreter;
import interpretation.InterpreterError;
import lexical_analysis.Lexer;
import lexical_analysis.Token;
import lexical_analysis.TokenType;
import syntactic_analysis.Parser;
import syntactic_analysis.Statement;

public class LuriaInterpreter {
	private static final Interpreter interpreter = new Interpreter();
	static boolean error = false;
	static boolean runtimeError = false;

	/*
	 * main(). On receipt of a single argument, i.e. a file name, the file is read;
	 * else the program enters the prompt state.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.exit(1);
		} else if (args.length == 1) {
			readFile(args[0]);
		} else {
			runPrompt();
		}
	}

	/*
	 * readFile() extracts the contents of the file as an array of binary data and
	 * decodes it as a String representing the source code. This is then passed to
	 * runLuria(), beginning the interpretation procedure. If an error occurs, this
	 * is flagged and the program will terminate.
	 */
	public static void readFile(String PATH) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(PATH));
		if (error)
			System.exit(1);
		if (runtimeError)
			System.exit(1);
		runLuria(new String(bytes, Charset.defaultCharset()));
	}

	/*
	 * runLuria() is the procedure passing the output of each interpretation phase
	 * to the next. If an error is flagged, the procedure is escaped before the
	 * Interpreter attempts to evaluate the syntax tree, avoiding a
	 * NullPointerException.
	 */
	public static void runLuria(String sourceString) {
		Lexer lexer = new Lexer(sourceString);
		List<Token> tokens = lexer.lexSourceCode();
		Parser parser = new Parser(tokens);
		List<Statement> statements = parser.parse();
		if (error)
			return;
		interpreter.interpret(statements);
	}

	/*
	 * runPrompt() allows for the entry and execution of Luria instructions, i.e.
	 * statements, one line at a time. Because the interpreter is stateful, having a
	 * memory of previous instructions, it is possible to compose simple programs in
	 * this mode. Upon detection of an error, that entry is disregarded and the
	 * program continues. This feature allows for simple, live testing of Luria
	 * syntax.
	 */
	public static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		while (true) {
			System.out.print(">> ");
			runLuria(reader.readLine());
			error = false;
		}
	}

	/*
	 * errorReport() is a private method called only within the LuriaInterpreter
	 * class. It concatenates a string for error printing composed of the line, i.e.
	 * location, at which the error occurred, the lexeme of the Token object
	 * following the erroneous syntax, and a relevant message.
	 */
	private static void errorReport(int line, String lexeme, String message) {
		System.err.println("[@ line " + line + "] Error @" + lexeme + ": " + message);
		error = true;
	}

	/*
	 * lexerError() collates error data from those occurring in the Lexer class and
	 * passes these to errorReport().
	 */
	public static void lexerError(int location, String message) {
		String empty = "";
		errorReport(location, empty, message);
	}

	/*
	 * parserError() collates error data from those occurring in the Parser class
	 * and passes these to errorReport().
	 */
	public static void parserError(Token token, String message) {
		if (token.type == TokenType.EOF) {
			errorReport(token.location, " EOF", message);
		} else {
			errorReport(token.location, " '" + token.lexeme + "'", message);
		}
	}

	/*
	 * parserError() collates error data from those occurring in the Interpreter
	 * class and passes these to errorReport().
	 */
	public static void interpreterError(InterpreterError error) {
		System.err.println(error.getMessage() + "\n[@ " + error.token.location + "]");
		runtimeError = true;
	}

}
