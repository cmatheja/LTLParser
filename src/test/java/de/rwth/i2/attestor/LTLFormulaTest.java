package de.rwth.i2.attestor;

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

		
	}

}
