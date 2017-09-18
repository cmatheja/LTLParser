package de.rwth.i2.attestor;

import de.rwth.i2.attestor.generated.analysis.DepthFirstAdapter;
import de.rwth.i2.attestor.generated.node.*;

/**
 * Created by christina on 14.09.17.
 */
public class OperatorEliminator extends DepthFirstAdapter {

    public void caseAFinallyLtlform(AFinallyLtlform node)
    {
        // Generate new until-node
        Node untilNode = new AUntilLtlform(new TLparen(), new ATermLtlform(new ATrueTerm(new TTrue())), new TUntil(), node.getLtlform(), new TRparen());

        // Add it to AST instead of the current node
        node.replaceBy(untilNode);

        // Proceed with this new node
        untilNode.apply(this);
    }

    public void caseAGloballyLtlform(AGloballyLtlform node)
    {
        // Generate new node for negated globally formula
        ANegStateform negNode = new ANegStateform(new TNeg(), node.getLtlform());
        // Generate new until formula
        AUntilLtlform untilNode = new AUntilLtlform(new TLparen(), new ATermLtlform(new ATrueTerm(new TTrue())), new TUntil(), new AStateformLtlform(negNode), new TRparen());
        // Negate the until node
        ANegStateform negUntil = new ANegStateform(new TNeg(), untilNode);
        AStateformLtlform negUntilLtl = new AStateformLtlform(negUntil);
        // Replace with this node and proceed with it
        node.replaceBy(negUntilLtl);

        negUntilLtl.apply(this);

    }

    public void caseAImpliesLtlform(AImpliesLtlform node)
    {
        // Negate left-hand side of implication
        ANegStateform negform1 = new ANegStateform(new TNeg(), node.getLeftform());
        AStateformLtlform negform1Ltl = new AStateformLtlform(negform1);
        // Disjunction of negated left-hand side and right-hand side
        AOrStateform orNode = new AOrStateform(new TLparen(), negform1Ltl, new TOr(), node.getRightform(), new TRparen());
        AStateformLtlform orNodeLtl = new AStateformLtlform(orNode);

        // Replace implication by newly generated node
        node.replaceBy(orNodeLtl);

        orNodeLtl.apply(this);

    }





}
