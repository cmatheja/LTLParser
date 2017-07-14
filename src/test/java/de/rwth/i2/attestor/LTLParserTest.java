package de.rwth.i2.attestor;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import de.rwth.i2.attestor.generated.parser.ParserException;

public class LTLParserTest {

	@BeforeClass
	public static void init() {
		
	}
	
	@Test
	public void testTermAcceptance() {
		LTLFormula formula;
	// Terms
		try{
			formula = new LTLFormula("F ");
			assertEquals(formula.toString(), "F  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			// Blanks ignored
			formula = new LTLFormula("F  ");
			assertEquals(formula.toString(), "F  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			// APs
			formula = new LTLFormula("5t");
			assertEquals(formula.toString(), "5t  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("t5t");
			assertEquals(formula.toString(), "t5t  ");
			//formula = new LTLFormula("T5T");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
	}
	
	@Test
	public void testTermRejectance() {
		try{
			new LTLFormula("T5T");
			fail("No exception raised.");
		} catch(ParserException e){
			
		} catch(Exception e){
			fail("No IOException or LexerException should occur.");
		}
	}
	
	@Test
	public void testStateFormAcceptance(){
		LTLFormula formula;
		try{
			formula = new LTLFormula("! ap5");
			assertEquals(formula.toString(), "! ap5  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("(ap1 & ap2)");
		assertEquals(formula.toString(), "( ap1 & ap2 )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("(ap1 | ap2)");
			assertEquals(formula.toString(), "( ap1 | ap2 )  ");
			} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
			}
		
		try{
		// Blanks ignored
		formula = new LTLFormula("(ap1&ap2)");
		assertEquals(formula.toString(), "( ap1 & ap2 )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("(ap1 & (ap2&ap3))");
		assertEquals(formula.toString(), "( ap1 & ( ap2 & ap3 ) )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("(ap1 & (ap2|ap3))");
			assertEquals(formula.toString(), "( ap1 & ( ap2 | ap3 ) )  ");
			} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
			}
	}
	
	@Test
	public void testLTLFormRejectance(){
		try{
			new LTLFormula("! (ap1 & ap2)");
			fail("No exception raised.");
		} catch(ParserException e){
			
		} catch(Exception e){
			fail("Negation only allowed with subsequent AP, not state formula.");
		}
		
		try{
			new LTLFormula("! X ap1");
			fail("No exception raised.");
		} catch(ParserException e){
			
		} catch(Exception e){
			fail("Negation only allowed with subsequent AP, not LTL formula.");
		}
		
		try{
			new LTLFormula("ap1 U ap2 R ap3)");
			fail("No exception raised.");
		} catch(ParserException e){
			
		} catch(Exception e){
			fail("Bracketing missing.");
		}
	}
	
	@Test
	public void testLTLFormAcceptance(){
		LTLFormula formula;
		try{
		formula = new LTLFormula("X ap5");
		assertEquals(formula.toString(), "X ap5  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("X (ap1 & ap2)");
		assertEquals(formula.toString(), "X ( ap1 & ap2 )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("XXXXX ap1");
		assertEquals(formula.toString(), "X X X X X ap1  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("((ap1& ! ap2) & X  ( X ap3 & ap4))");
		assertEquals(formula.toString(), "( ( ap1 & ! ap2 ) & X ( X ap3 & ap4 ) )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("(ap1 Uap2)");
			assertEquals(formula.toString(), "( ap1 U ap2 )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("(ap1 UXap2)");
			assertEquals(formula.toString(), "( ap1 U X ap2 )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("(ap1 U (ap2 U ap3))");
			assertEquals(formula.toString(), "( ap1 U ( ap2 U ap3 ) )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("(ap1 R ap2)");
			assertEquals(formula.toString(), "( ap1 R ap2 )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
	}
	
	@Test
	public void testASTConstruction() {
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("(ap1 U ap2)");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		formula.getTableauSubformulae(formula.getASTRoot());
	}
	
}
