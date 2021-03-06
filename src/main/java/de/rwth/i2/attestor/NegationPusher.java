package de.rwth.i2.attestor;

import de.rwth.i2.attestor.generated.analysis.DepthFirstAdapter;
import de.rwth.i2.attestor.generated.node.*;

/**
 * The negation pusher walks the AST and pushes any negation inside until there is
 * only negation at term level.
 *
 * Created by christina on 15.09.17.
 */
public class NegationPusher extends DepthFirstAdapter {

    public void caseAStateformLtlform(AStateformLtlform node)
    {
        Node stateForm = node.getStateform();
        if(stateForm instanceof ANegStateform){
            // Push negation inside
            Node nonNegForm = ((ANegStateform) stateForm).getLtlform();
            Node newNode = handlePushCases(nonNegForm);

            // Adapt only, if the negation was not already at term level
            if(nonNegForm != newNode) {
                node.replaceBy(newNode);
                newNode.apply(this);
            }

        } else {
            super.caseAStateformLtlform(node);
        }
    }


    /**
     * A helper class that handles the different transformations whenever a negated formula was detected.
     * @param nonNegForm, the formula with negation already removed
     * @return the transformed formula, where negation was pushed inside
     */
    private Node handlePushCases(Node nonNegForm) {

        // In all other cases the subformula is a term, thus do nothing (as negation cannot be pushed inside furthermore)
        if(nonNegForm instanceof AStateformLtlform && ((AStateformLtlform) nonNegForm).getStateform() instanceof ANegStateform){
            return ((ANegStateform) ((AStateformLtlform) nonNegForm).getStateform()).getLtlform();

        } else if(nonNegForm instanceof AStateformLtlform && ((AStateformLtlform) nonNegForm).getStateform() instanceof AOrStateform){
            AOrStateform orForm = (AOrStateform) ((AStateformLtlform) nonNegForm).getStateform();
            PLtlform form1 = orForm.getLeftform();
            PLtlform form2 = orForm.getRightform();

            // Negate both subformulae
            AStateformLtlform negForm1Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form1));
            AStateformLtlform negForm2Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form2));

            // Transform or into and statement (with negated subformulae)
            AStateformLtlform andFormLtl = new AStateformLtlform(new AAndStateform(new TLparen(), negForm1Ltl, new TAnd(), negForm2Ltl, new TRparen()));

            return andFormLtl;

        } else if(nonNegForm instanceof AStateformLtlform && ((AStateformLtlform) nonNegForm).getStateform() instanceof AAndStateform){

            AAndStateform andForm = (AAndStateform) ((AStateformLtlform) nonNegForm).getStateform();
            PLtlform form1 = andForm.getLeftform();
            PLtlform form2 = andForm.getRightform();

            // Negate both subformulae
            AStateformLtlform negForm1Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form1));
            AStateformLtlform negForm2Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form2));

            // Transform and into or statement (with negated subformulae)
            AStateformLtlform orFormLtl = new AStateformLtlform(new AOrStateform(new TLparen(), negForm1Ltl, new TOr(), negForm2Ltl, new TRparen()));

            return orFormLtl;

        } else if(nonNegForm instanceof ANextLtlform){

            // Push negation inside without further changes
            PLtlform form = ((ANextLtlform) nonNegForm).getLtlform();
            AStateformLtlform negForm = new AStateformLtlform(new ANegStateform(new TNeg(), form));
            ANextLtlform nextLtlform = new ANextLtlform(new TNext(), negForm);

            return nextLtlform;

        }else if(nonNegForm instanceof AUntilLtlform){

            PLtlform form1 = ((AUntilLtlform) nonNegForm).getLeftform();
            PLtlform form2 = ((AUntilLtlform) nonNegForm).getRightform();

            // Negate both subformulae
            AStateformLtlform negForm1Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form1));
            AStateformLtlform negForm2Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form2));

            // Conjunct both by release operator
            AReleaseLtlform releaseForm = new AReleaseLtlform(new TLparen(), negForm1Ltl, new TRelease(), negForm2Ltl, new TRparen());

            return releaseForm;

        } else if(nonNegForm instanceof AReleaseLtlform){

            PLtlform form1 = ((AReleaseLtlform) nonNegForm).getLeftform();
            PLtlform form2 = ((AReleaseLtlform) nonNegForm).getRightform();

            // Negate both subformulae
            AStateformLtlform negForm1Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form1));
            AStateformLtlform negForm2Ltl = new AStateformLtlform(new ANegStateform(new TNeg(), form2));

            // Conjunct both by until operator
            AUntilLtlform untilForm = new AUntilLtlform(new TLparen(), negForm1Ltl, new TUntil(), negForm2Ltl, new TRparen());

            return untilForm;
        }

        return nonNegForm;
    }

}
