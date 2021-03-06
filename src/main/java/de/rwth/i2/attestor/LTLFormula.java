package de.rwth.i2.attestor;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.rwth.i2.attestor.generated.lexer.*;
import de.rwth.i2.attestor.generated.node.*;
import de.rwth.i2.attestor.generated.node.Start;
import de.rwth.i2.attestor.generated.parser.*;

/**
 * Holds an LTL formula, its corresponding AST obtained from parsing, and
 * its formula walker. The class provides formula walking capabilities, that
 * already tailored to the tableau method for model checking.
 * 
 * @author christina
 *
 */

public class LTLFormula {

	private String formulaString;
	// Root node of the AST
	private Start SableCCast;

	// The list of atomic propositions contained in the formula
	private List<String> apList;
	
	private FormulaWalker walker;

	public LTLFormula(String formula) throws ParserException, LexerException, IOException{
		formulaString = formula;
		walker = new FormulaWalker();
		// try {
            /* Form the AST */
            Lexer lexer = new Lexer(new PushbackReader(
            		new StringReader(formulaString), 1024));
            Parser parser = new Parser(lexer);
            SableCCast = parser.parse() ;
 
            /* Collect all atomic propositions contained in the formula. */
            APCollector apCollector = new APCollector();
            SableCCast.apply(apCollector);
            apList = apCollector.getAps();
	}
	
	/**
	 * This procedure returns the subformulae of the formula associated with node,
	 * that are to be considered by the tableau method in the current step.
	 * 
	 * Note that the successor list of the tree walker is cleared each time this
	 * method is called in order to obtain only the direct subformulae of the input
	 * node.
	 * 
	 * @param node the AST node associated to the formula currently in process
	 * @return the list of subformulae, this list contains the subformula in the
	 * order of appearance and, in case the input node corresponds to an until or
	 * release formula, the unrolled formula as last element
	 */
	public ArrayList<Node> getTableauSubformulae(Node node){
		walker.resetSuccessors();
		node.apply(walker);
		return walker.getSuccessors();

	}

	public List<String> getApList() {
		return apList;
	}
	
	public String toString(){
		return this.SableCCast.toString();
	}

	public Start getASTRoot() {
		
		return this.SableCCast;
	}

	public FormulaWalker getFormulaWalker() {

		return this.walker;
	}

	public String getFormulaString() {
		return formulaString;
	}

	public boolean equals(LTLFormula formula){

		return this.formulaString.replaceAll(" ", "") == formula.getFormulaString().replaceAll(" ", "");
	}

	public int hashCode(){
		return this.formulaString.replaceAll(" ", "").hashCode();
	}

	/**
	 * This method transforms the formula into PNF (i.e. adapts the formulae's AST accordingly).
	 */
	public void toPNF(){
		// First eliminate all non-PNF operators (finally, globally, implication)
		OperatorEliminator operatorEliminator = new OperatorEliminator();
		SableCCast.apply(operatorEliminator);

		// Then push negation inside
		NegationPusher negPusher = new NegationPusher();
		SableCCast.apply(negPusher);
	}
}
