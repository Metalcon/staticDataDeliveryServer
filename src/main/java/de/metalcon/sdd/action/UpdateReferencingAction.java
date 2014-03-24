package de.metalcon.sdd.action;

import java.util.Set;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class UpdateReferencingAction extends Action {

    private long nodeId;

    private Set<String> modifiedDetails;

    public UpdateReferencingAction(
            long nodeId,
            Set<String> modifiedDetails) {
        this.nodeId = nodeId;
        this.modifiedDetails = modifiedDetails;
    }

    @Override
    public void runAction(Sdd sdd) throws SddException {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean equals(Object other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

}
