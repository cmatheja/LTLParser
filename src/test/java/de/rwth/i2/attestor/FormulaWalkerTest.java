package de.rwth.i2.attestor;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import de.rwth.i2.attestor.generated.node.AAtomicpropTerm;
import de.rwth.i2.attestor.generated.node.ANextLtlform;
import de.rwth.i2.attestor.generated.node.ATermLtlform;
import de.rwth.i2.attestor.generated.node.Node;

public class FormulaWalkerTest {
	
	@Test
	public void testASTModification(){
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("({dll} U {tree})");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		FormulaWalker walker = formula.getFormulaWalker();
		Node node = formula.getASTRoot().getPLtlform();
		
		// Until node not yet linked to any next formula
		assertFalse(walker.getAdditionalNextFormulae().containsKey(node));
		formula.getTableauSubformulae(node);
		// Now pair of until formula and next formula is added
		assertTrue(walker.getAdditionalNextFormulae().containsKey(node));
		Node nextNode = walker.getAdditionalNextFormulae().get(node);
		// The next node is of correct type
		assertTrue(nextNode instanceof ANextLtlform);
		
		formula.getTableauSubformulae(node);
		// After repeated formula walk, the hash for the until node does not change anymore
		assertEquals(nextNode, walker.getAdditionalNextFormulae().get(node));
	}
	
	@Test
	public void testFormulaWalkerAtomicProp(){
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("{dll}");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		ArrayList<Node> successors = formula.getTableauSubformulae(formula.getASTRoot());
		assertTrue(successors.size() == 1);
		
		assertTrue(successors.get(0) instanceof AAtomicpropTerm);

		assertEquals(successors.get(0).toString(), "{ dll } ");
	}
	
	@Test
	public void testFormulaWalkerAnd(){
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("({dll} & {tree})");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		ArrayList<Node> successors = formula.getTableauSubformulae(formula.getASTRoot());
		assertTrue(successors.size() == 2);
		
		for(Node successor : successors){
			assertTrue(successor instanceof ATermLtlform);
		}
		
		assertEquals(successors.get(0).toString(), "{ dll } ");
		assertEquals(successors.get(1).toString(), "{ tree } ");
		
	}
	
	@Test
	public void testFormulaWalkerUntil(){
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("({dll} U {tree})");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		ArrayList<Node> successors = formula.getTableauSubformulae(formula.getASTRoot());
		assertTrue(successors.size() == 3);
		

		assertEquals(successors.get(0).toString(), "{ dll } ");
		assertEquals(successors.get(1).toString(), "{ tree } ");
		assertEquals(successors.get(2).toString(), "X ");
		
		// Make sure that walking newly generated next node returns the original until node
		ArrayList<Node> nextSuccessors = formula.getTableauSubformulae(successors.get(2));
		assertEquals(formula.getASTRoot().getPLtlform(), nextSuccessors.get(0));
		
	}

}
