package de.rwth.i2.attestor;

import java.util.ArrayList;

import com.google.common.collect.HashBiMap;

import de.rwth.i2.attestor.generated.analysis.AnalysisAdapter;
import de.rwth.i2.attestor.generated.node.AAndStateform;
import de.rwth.i2.attestor.generated.node.AAtomicpropTerm;
import de.rwth.i2.attestor.generated.node.AFalseTerm;
import de.rwth.i2.attestor.generated.node.ANegStateform;
import de.rwth.i2.attestor.generated.node.ANextLtlform;
import de.rwth.i2.attestor.generated.node.AOrStateform;
import de.rwth.i2.attestor.generated.node.AStateformLtlform;
import de.rwth.i2.attestor.generated.node.ATermLtlform;
import de.rwth.i2.attestor.generated.node.ATrueTerm;
import de.rwth.i2.attestor.generated.node.AUntilLtlform;
import de.rwth.i2.attestor.generated.node.Node;
import de.rwth.i2.attestor.generated.node.Start;
import de.rwth.i2.attestor.generated.node.TNext;

/**
 * The LTL formula interpreter, that is tailored to the tableau method for
 * model checking. That is, walking an input formula, its subformulae for the
 * tableau method are provided (including the unrolled formulae for release and
 * until operator).
 * 
 * TODO: check if still necessary? tableau rules switch may incorporate this functionality!
 * 
 * @author christina
 *
 */

public class FormulaWalker extends AnalysisAdapter {
	
	// Holds the additional next formulae obtained while unrolling release and until
	HashBiMap<Node, ANextLtlform> additionalNextFormulae;
	
	// Collects the successor nodes for the tableau method
	ArrayList<Node> successors;
	
	public FormulaWalker(){
		additionalNextFormulae = HashBiMap.create();
		successors = new ArrayList<Node>();
	}
	
    public void caseStart(Start node)
    {
        node.getPLtlform().apply(this);
        
    }
    
    public void caseAStateformLtlform(AStateformLtlform node)
    {

        if(node.getStateform() != null)
        {
            node.getStateform().apply(this);
        }

    }
    
    public void caseANextLtlform(ANextLtlform node)
    {
    	// Check first, if the next formula is an original one
    	if(additionalNextFormulae.containsValue(node)){
    		// If not, proceed with the associated until formula.
    		// Note that this is a workaround, because a helper until formula was used upon generating
    		// the new next formula (to preserve the AST tree property)
    		successors.add(additionalNextFormulae.inverse().get(node));
    	} else {

    		if(node.getLtlform() != null)
    		{
    			successors.add(node.getLtlform());
    		}
    	}
    }
    
    public void caseATermLtlform(ATermLtlform node)
    {
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
    }
    
    public void caseAUntilLtlform(AUntilLtlform node)
    {

    	if(node.getLeftform() != null)
        {
    		successors.add(node.getLeftform());
        }
    	
        if(node.getRightform() != null)
        {
        	successors.add(node.getRightform());
        }
        
        ANextLtlform nextNode;
        
        // Check if the unrolled until formula is already present
        if(additionalNextFormulae.containsKey(node)){
        	nextNode = additionalNextFormulae.get(node);
        } else {
        	// Generate new AST node for X node
        	TNext nextToken = new TNext();
        	
        	// CARE: As violation of AST tree property is not possible, set dummy until formula
        	// Make sure that the original until formula (the hash map key) is used _at all times_
        	AUntilLtlform helperNode = new AUntilLtlform();
        	nextNode = new ANextLtlform(nextToken ,helperNode);
        	
        	// Add to hashmap
        	additionalNextFormulae.put(node, nextNode);
        
        }
        
        // Collect additionally X node
        successors.add(nextNode);
    }
    
    public void caseAReleaseLtlform(AUntilLtlform node)
    {

    	if(node.getLeftform() != null)
        {
        	successors.add(node.getLeftform());
        }
    	
        if(node.getRightform() != null)
        {
        	successors.add(node.getRightform());
        }
        
        ANextLtlform nextNode;
        
        // Check if the unrolled until formula is already present
        if(additionalNextFormulae.containsKey(node)){
        	nextNode = additionalNextFormulae.get(node);
        } else {
        	// Generate new AST node for X node
        	TNext nextToken = new TNext();
        	
        	// CARE: As violation of AST tree property is not possible, set dummy until formula
        	// Make sure that the original until formula (the hash map key) is used _at all times_
        	AUntilLtlform helperNode = new AUntilLtlform();
        	nextNode = new ANextLtlform(nextToken ,helperNode);
        	
        	additionalNextFormulae.put(node, nextNode);
        
        }
        
        // Collect additionally X node
        successors.add(nextNode);
    }
    
    public void caseANegStateform(ANegStateform node)
    {
    	successors.add(node);
    }

    
    public void caseAAndStateform(AAndStateform node)
    {
        if(node.getLeftform() != null)
        {
        	successors.add(node.getLeftform());
        }

        if(node.getRightform() != null)
        {
        	successors.add(node.getRightform());
        }

    }
    
    public void caseAOrStateform(AOrStateform node)
    {

        if(node.getLeftform() != null)
        {
            successors.add(node.getLeftform());
        }
        
        if(node.getRightform() != null)
        {
            successors.add(node.getRightform());
        }
    }
    
    public void caseATrueTerm(ATrueTerm node)
    {
    	successors.add(node);
    }
    
    public void caseAFalseTerm(AFalseTerm node)
    {
    	successors.add(node);
    }
    
    public void caseAAtomicpropTerm(AAtomicpropTerm node)
    {
    	successors.add(node);
    }

	public HashBiMap<Node, ANextLtlform> getAdditionalNextFormulae() {
		return this.additionalNextFormulae;
	}

	public void resetSuccessors() {
		this.successors = new ArrayList<Node>();		
	}

	public ArrayList<Node> getSuccessors() {
		return this.successors;
	}

}
