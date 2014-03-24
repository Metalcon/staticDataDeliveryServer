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
        if (other == this) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        UpdateReferencingAction o = (UpdateReferencingAction) other;
        return nodeId == o.nodeId && modifiedDetails.equals(o.modifiedDetails);
    }

    @Override
    public int hashCode() {
        int hash = 19236;
        int mult = 211;

        hash = hash * mult + ((Long) nodeId).hashCode();
        hash = hash * mult + modifiedDetails.hashCode();

        return hash;
    }

}
