package testsuite;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import lexical_analysis.Lexer;
import lexical_analysis.Token;
import lexical_analysis.TokenType;

import static lexical_analysis.TokenType.*;

public class UnitTest {
	static Lexer LEXER;
	static List<Token> TOKEN_OUTPUT = new ArrayList<>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String LEXER_TEST_CODE = "src\\testsuite\\LEXER_TEST_CODE.txt";
		byte[] bytes = Files.readAllBytes(Paths.get(LEXER_TEST_CODE));
		String SOURCE_CODE = new String(bytes, Charset.defaultCharset());
		LEXER = new Lexer(SOURCE_CODE);
		TOKEN_OUTPUT = LEXER.lexSourceCode();
	}

	@Test
	public void ASTERISK() {
		TOKEN_OUTPUT = LEXER.lexSourceCode();
		TokenType t = TOKEN_OUTPUT.get(0).type;
		assertEquals(ASTERISK, t);
	}
	
	@Test
	public void COMMA() {
		TOKEN_OUTPUT = LEXER.lexSourceCode();
		TokenType t = TOKEN_OUTPUT.get(1).type;
		System.out.println(TOKEN_OUTPUT.get(1).lexeme);
		System.out.println(TOKEN_OUTPUT.get(1).type);
		System.out.println(TOKEN_OUTPUT.get(1).location);
		System.out.println(TOKEN_OUTPUT.get(1).literal);

		assertEquals(COMMA, t);
	}
	
	@Test
	public void EXPONENT() {
		TOKEN_OUTPUT = LEXER.lexSourceCode();
		TokenType t = TOKEN_OUTPUT.get(2).type;
		assertEquals(EXPONENT, t);
	}
	
	@Test
	public void FORWARD_SLASH() {
		TOKEN_OUTPUT = LEXER.lexSourceCode();
		TokenType t = TOKEN_OUTPUT.get(3).type;
		assertEquals(FORWARD_SLASH, t);
	}
	
	@Test
	public void FULL_STOP() {
		TOKEN_OUTPUT = LEXER.lexSourceCode();
		TokenType t = TOKEN_OUTPUT.get(4).type;
		assertEquals(FULL_STOP, t);
	}
	
	@Test
	public void PRINT() {
		TOKEN_OUTPUT = LEXER.lexSourceCode();
		TokenType t = TOKEN_OUTPUT.get(33).type;
		assertEquals(PRINT, t);
	}

}
