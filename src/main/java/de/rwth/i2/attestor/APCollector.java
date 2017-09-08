package de.rwth.i2.attestor;

import de.rwth.i2.attestor.generated.analysis.DepthFirstAdapter;
import de.rwth.i2.attestor.generated.node.AAtomicpropTerm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christina on 08.09.17.
 */
public class APCollector extends DepthFirstAdapter {

    List<String> aps;

    public APCollector(){
        aps = new ArrayList<String>();
    }

    public List<String> getAps() {
        return aps;
    }

    public void caseAAtomicpropTerm(AAtomicpropTerm node)
    {
        if(node.getAtomicprop() != null)
        {
            aps.add(node.getAtomicprop().toString().trim());
        }
    }
}
