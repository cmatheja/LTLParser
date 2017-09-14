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
			formula = new LTLFormula("false ");
			assertEquals(formula.toString(), "false  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			// Blanks ignored
			formula = new LTLFormula("false  ");
			assertEquals(formula.toString(), "false  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			// APs
			formula = new LTLFormula("{tree}");
			assertEquals(formula.toString(), "{ tree }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{btree}");
			assertEquals(formula.toString(), "{ btree }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{sll}");
			assertEquals(formula.toString(), "{ sll }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{dll}");
			assertEquals(formula.toString(), "{ dll }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{_v == dll}");
			assertEquals(formula.toString(), "{ _v == dll }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{_v._v != dll}");
			assertEquals(formula.toString(), "{ _v._v != dll }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{visited}");
			assertEquals(formula.toString(), "{ visited }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			formula = new LTLFormula("{visited(x)}");
			assertEquals(formula.toString(), "{ visited(x) }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("{visited($x1x)}");
			assertEquals(formula.toString(), "{ visited($x1x) }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			formula = new LTLFormula("{visited($x_1x)}");
			assertEquals(formula.toString(), "{ visited($x_1x) }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			formula = new LTLFormula("{isReachable(x,y)}");
			assertEquals(formula.toString(), "{ isReachable(x,y) }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			formula = new LTLFormula("{isReachable(x,y,[$x])}");
			assertEquals(formula.toString(), "{ isReachable(x,y,[$x]) }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			formula = new LTLFormula("{isReachable(x,y,[$x,$y])}");
			assertEquals(formula.toString(), "{ isReachable(x,y,[$x,$y]) }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		try{
			// APs
			formula = new LTLFormula("{terminated}");
			assertEquals(formula.toString(), "{ terminated }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}

		
	}
	
	@Test
	public void testTermRejectance() {
		try{
			new LTLFormula("{true}");
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
			formula = new LTLFormula("! {tree}");
			assertEquals(formula.toString(), "! { tree }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("({tree} & {dll})");
		assertEquals(formula.toString(), "( { tree } & { dll } )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("({tree} | {dll})");
			assertEquals(formula.toString(), "( { tree } | { dll } )  ");
			} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
			}
		
		try{
		// Blanks ignored
		formula = new LTLFormula("({tree}&{dll})");
		assertEquals(formula.toString(), "( { tree } & { dll } )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("({tree} & ({tree}&{dll}))");
		assertEquals(formula.toString(), "( { tree } & ( { tree } & { dll } ) )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("({tree} & ({sll}|{dll}))");
			assertEquals(formula.toString(), "( { tree } & ( { sll } | { dll } ) )  ");
			} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
			}
	}
	
	@Test
	public void testLTLFormRejectance(){
		try{
			new LTLFormula("! ({tree} & {dll})");
			fail("No exception raised.");
		} catch(ParserException e){
			
		} catch(Exception e){
			fail("Negation only allowed with subsequent AP, not state formula.");
		}
		
		try{
			new LTLFormula("! X {tree}");
			fail("No exception raised.");
		} catch(ParserException e){
			
		} catch(Exception e){
			fail("Negation only allowed with subsequent AP, not LTL formula.");
		}
		
		try{
			new LTLFormula("{tree} U {sll} R {dll})");
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
		formula = new LTLFormula("X {tree}");
		assertEquals(formula.toString(), "X { tree }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("X ({tree} & {sll})");
		assertEquals(formula.toString(), "X ( { tree } & { sll } )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("XXXXX {tree}");
		assertEquals(formula.toString(), "X X X X X { tree }  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
		formula = new LTLFormula("(({tree}& ! {sll}) & X  ( X {btree} & {dll}))");
		assertEquals(formula.toString(), "( ( { tree } & ! { sll } ) & X ( X { btree } & { dll } ) )  ");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("({tree} U{sll})");
			assertEquals(formula.toString(), "( { tree } U { sll } )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("({tree} UX{sll})");
			assertEquals(formula.toString(), "( { tree } U X { sll } )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("({tree} U ({sll} U {dll}))");
			assertEquals(formula.toString(), "( { tree } U ( { sll } U { dll } ) )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		try{
			formula = new LTLFormula("({tree} R {sll})");
			assertEquals(formula.toString(), "( { tree } R { sll } )  ");
		} catch(Exception e) {
				fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
	}
	
	@Test
	public void testASTConstruction() {
		LTLFormula formula = null;
		try{
			formula = new LTLFormula("({tree} U {tree})");
		} catch(Exception e) {
			fail("Formula should parse correctly. No Parser and Lexer exception expected!");
		}
		
		formula.getTableauSubformulae(formula.getASTRoot());
	}
	
}
