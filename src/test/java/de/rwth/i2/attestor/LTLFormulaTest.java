package de.rwth.i2.attestor;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import de.rwth.i2.attestor.generated.node.Node;

public class LTLFormulaTest {
	
	@Test
	public void getTableauSubformulaeSuccResetTest(){
		
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("({tree} & {dll})");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		ArrayList<Node> successors = formula.getTableauSubformulae(formula.getASTRoot());
		assertTrue(successors.size() == 2);
		
		successors = formula.getTableauSubformulae(formula.getASTRoot());
		assertTrue(successors.size() == 2);

		assertTrue(formula.getApList().size() == 2);
		assertEquals(formula.getApList().get(0), "{ tree }");
		assertEquals(formula.getApList().get(1), "{ dll }");
	}

	@Test
	public void collectAPTest(){
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("({tree} & {dll})");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		assertTrue(formula.getApList().size() == 2);
		assertEquals(formula.getApList().get(0), "{ tree }");
		assertEquals(formula.getApList().get(1), "{ dll }");

		LTLFormula formula2 = null;
		try{
			formula2 = new LTLFormula("({tree} U ({sll} R {dll}))");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		assertTrue(formula2.getApList().size() == 3);
		assertEquals(formula2.getApList().get(0), "{ tree }");
		assertEquals(formula2.getApList().get(1), "{ sll }");
		assertEquals(formula2.getApList().get(2), "{ dll }");
	}

}
