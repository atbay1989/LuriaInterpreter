package luria;

import java.io.BufferedReader;
// Java imports
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import interpretation.Interpreter;
import lexical_analysis.Lexer;
import lexical_analysis.Token;
import lexical_analysis.TokenType;
import syntactic_analysis.Parser;
import syntactic_analysis.RuntimeError;
import syntactic_analysis.Statement;

public class Luria {
	// fields
	private static final Interpreter interpreter = new Interpreter();
	static boolean error = false;
	static boolean runtimeError = false;

	// main
	public static void main(String[] args) throws IOException {
		readFile("src\\testsuite\\readtest.txt");
/*		if (args.length > 1) {
			System.exit(666);
		} else if (args.length == 1) {
			readFile(args[0]);
		} else {
			runPrompt();
		}*/
	}

	// read and execute file from file path
	public static void readFile(String PATH) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(PATH));
		if (error)
			System.exit(667);
		if (runtimeError)
			System.exit(668);
		runLuria(new String(bytes, Charset.defaultCharset()));
	}

	// run
	public static void runLuria(String sourceString) {
		Lexer lexer = new Lexer(sourceString);
		List<Token> tokens = lexer.lexSourceCode();
		Parser parser = new Parser(tokens);
		List<Statement> statements = parser.parse();
		if (error)
			return;
		interpreter.interpret(statements);
	}

	public static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		while (true) {
			System.out.print("> ");
			runLuria(reader.readLine());
			error = false;
		}
	}

	// error handling

	private static void report(int line, String location, String message) {
		System.err.println("[line " + line + "] Error" + location + ": " + message);
		error = true;
	}
	
	public static void lexerError(int location, String message) {
		report(location, "", message);
	}
	
	public static void parserError(Token token, String message) {
		if (token.type == TokenType.EOF) {
			report(token.location, " at EOF", message);
		} else {
			report(token.location, " at '" + token.lexeme + "'", message);
		}
	}

	public static void runtimeError(RuntimeError error) {
		System.err.println(error.getMessage() + "\n[line " + error.token.location + "]");
		runtimeError = true;
	}

}
