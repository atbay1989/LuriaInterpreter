package luria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import evaluator.Interpreter;
import syntacticanalysis.Parser;
import syntacticanalysis.RuntimeError;
import syntacticanalysis.Scanner;
import syntacticanalysis.Statement;
import syntacticanalysis.Token;
import syntacticanalysis.TokenType;

public class Luria {
	// fields
	private static final Interpreter interpreter = new Interpreter();
	static boolean error = false;
	static boolean runtimeError = false;
	
	// main
	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.out.println("Usage: Luria [script]");
			System.exit(666);
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			runPrompt();
		}
	}
	
	// read and execute file from file path
	public static void runFile(String path) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		
		if (error) System.exit(667);
		if (runtimeError) System.exit(668);
		
		run(new String(bytes, Charset.defaultCharset()));
	}

	// read and execute from prompt
	public static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		while (true) {
			System.out.print("> ");
			run(reader.readLine());
			error = false;
		}
	}
	
	// run
	public static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();		
	    Parser parser = new Parser(tokens);                    
	    //Expression expression = parser.parse();
	    List<Statement> statements = parser.parse();            
	    if (error) return;
	    //interpreter.interpret(expression);
	    interpreter.interpret(statements);
	    //System.out.println(new PrettyPrinter().print(expression));	
/*		if (error) System.exit(667);		
		// print tokens
		for (Token token : tokens) {
			System.out.println(token);
		}*/
	}
	
	public static void error(int line, String message) {
		report(line, "", message);
	}

	private static void report(int line, String location, String message) {
		System.err.println("[line " + line + "] Error" + location + ": " + message);
		error = true;
	}

	public static void error(Token token, String message) {
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
