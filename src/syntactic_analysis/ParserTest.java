package syntactic_analysis;

import java.util.List;

import javax.annotation.processing.Generated;

import org.junit.Test;
import org.junit.tools.configuration.base.MethodRef;

import lexical_analysis.Token;
import lexical_analysis.TokenType;
import mockit.Deencapsulation;

@Generated(value = "org.junit-tools-1.1.0")
public class ParserTest {

	private Parser createTestSubject() {
		return new Parser(null);
	}

	@MethodRef(name = "addition", signature = "()QExpression;")
	@Test
	public void testAddition() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "addition");
	}

	@MethodRef(name = "and", signature = "()QExpression;")
	@Test
	public void testAnd() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "and");
	}

	@MethodRef(name = "assignment", signature = "()QExpression;")
	@Test
	public void testAssignment() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "assignment");
	}

	@MethodRef(name = "block", signature = "()QList<QStatement;>;")
	@Test
	public void testBlock() throws Exception {
		Parser testSubject;
		List<Statement> result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "block");
	}

	@MethodRef(name = "call", signature = "()QExpression;")
	@Test
	public void testCall() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "call");
	}

	@MethodRef(name = "check", signature = "(QTokenType;)Z")
	@Test
	public void testCheck() throws Exception {
		Parser testSubject;
		TokenType t = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "check", new Object[] { TokenType.class });
	}

	@MethodRef(name = "comparison", signature = "()QExpression;")
	@Test
	public void testComparison() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "comparison");
	}

	@MethodRef(name = "declaration", signature = "()QStatement;")
	@Test
	public void testDeclaration() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "declaration");
	}

	@MethodRef(name = "end", signature = "()Z")
	@Test
	public void testEnd() throws Exception {
		Parser testSubject;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "end");
	}

	@MethodRef(name = "equality", signature = "()QExpression;")
	@Test
	public void testEquality() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "equality");
	}

/*	@MethodRef(name = "error", signature = "(QToken;QString;)QParserError;")
	@Test
	public void testError() throws Exception {
		Parser testSubject;
		Token token = null;
		String error = "";
		ParserError result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "error", new Object[] { Token.class, error });
	}*/

	@MethodRef(name = "exponent", signature = "()QExpression;")
	@Test
	public void testExponent() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "exponent");
	}

	@MethodRef(name = "expression", signature = "()QExpression;")
	@Test
	public void testExpression() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "expression");
	}

	@MethodRef(name = "expressionStatement", signature = "()QStatement;")
	@Test
	public void testExpressionStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "expressionStatement");
	}

	@MethodRef(name = "functionDeclaration", signature = "()QStatement;")
	@Test
	public void testFunctionDeclaration() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "functionDeclaration");
	}

	@MethodRef(name = "ifStatement", signature = "()QStatement;")
	@Test
	public void testIfStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "ifStatement");
	}

	@MethodRef(name = "literal", signature = "()QExpression;")
	@Test
	public void testLiteral() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "literal");
	}

	@MethodRef(name = "lookahead", signature = "()QToken;")
	@Test
	public void testLookahead() throws Exception {
		Parser testSubject;
		Token result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "lookahead");
	}

	@MethodRef(name = "lookprevious", signature = "()QToken;")
	@Test
	public void testLookprevious() throws Exception {
		Parser testSubject;
		Token result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "lookprevious");
	}

	@MethodRef(name = "match", signature = "([QTokenType;)Z")
	@Test
	public void testMatch() throws Exception {
		Parser testSubject;
		TokenType[] types = new TokenType[] { null };
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "match", new Object[] { TokenType[].class });
	}

	@MethodRef(name = "multiplication", signature = "()QExpression;")
	@Test
	public void testMultiplication() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "multiplication");
	}

	@MethodRef(name = "next", signature = "()QToken;")
	@Test
	public void testNext() throws Exception {
		Parser testSubject;
		Token result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "next");
	}

	@MethodRef(name = "or", signature = "()QExpression;")
	@Test
	public void testOr() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "or");
	}

	@MethodRef(name = "parse", signature = "()QList<QStatement;>;")
	@Test
	public void testParse() throws Exception {
		Parser testSubject;
		List<Statement> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.parse();
	}

	@MethodRef(name = "printStatement", signature = "()QStatement;")
	@Test
	public void testPrintStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "printStatement");
	}

	@MethodRef(name = "process", signature = "(QTokenType;QString;)QToken;")
	@Test
	public void testProcess() throws Exception {
		Parser testSubject;
		TokenType type = null;
		String error = "";
		Token result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "process", new Object[] { TokenType.class, error });
	}

	@MethodRef(name = "readBooleanStatement", signature = "()QStatement;")
	@Test
	public void testReadBooleanStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "readBooleanStatement");
	}

	@MethodRef(name = "readNumberStatement", signature = "()QStatement;")
	@Test
	public void testReadNumberStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "readNumberStatement");
	}

	@MethodRef(name = "readStringStatement", signature = "()QStatement;")
	@Test
	public void testReadStringStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "readStringStatement");
	}

	@MethodRef(name = "returnStatement", signature = "()QStatement;")
	@Test
	public void testReturnStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "returnStatement");
	}

	@MethodRef(name = "statement", signature = "()QStatement;")
	@Test
	public void testStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "statement");
	}

	@MethodRef(name = "sync", signature = "()V")
	@Test
	public void testSync() throws Exception {
		Parser testSubject;

		// default test
		testSubject = createTestSubject();
		Deencapsulation.invoke(testSubject, "sync");
	}

	@MethodRef(name = "unary", signature = "()QExpression;")
	@Test
	public void testUnary() throws Exception {
		Parser testSubject;
		Expression result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "unary");
	}

	@MethodRef(name = "variableDeclaration", signature = "()QStatement;")
	@Test
	public void testVariableDeclaration() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "variableDeclaration");
	}

	@MethodRef(name = "whileStatement", signature = "()QStatement;")
	@Test
	public void testWhileStatement() throws Exception {
		Parser testSubject;
		Statement result;

		// default test
		testSubject = createTestSubject();
		result = Deencapsulation.invoke(testSubject, "whileStatement");
	}
}