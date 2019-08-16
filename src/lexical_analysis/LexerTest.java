package lexical_analysis;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerTest {
	static Lexer LEXER;
	static List<Token> TOKEN_OUTPUT = new ArrayList<>();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		String LEXER_TEST_CODE = "src\\testsuite\\LEXER_TEST_CODE.txt";
		byte[] bytes = Files.readAllBytes(Paths.get(LEXER_TEST_CODE));
		String SOURCE_CODE = new String(bytes, Charset.defaultCharset());
		LEXER = new Lexer(SOURCE_CODE);
		TOKEN_OUTPUT = LEXER.lexSourceCode();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testLexer() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testProcess() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAlphabeticChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testNumericChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAlphanumericChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAddTokenTokenType() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAddTokenTokenTypeObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testMatch() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLookahead() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testDoubleLookahead() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testEnd() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLexSourceCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLexToken() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLexNumber() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLexString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testLexSignifier() {
		fail("Not yet implemented"); // TODO
	}

}
