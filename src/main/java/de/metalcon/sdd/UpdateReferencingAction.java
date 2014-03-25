package de.metalcon.sdd;

import java.util.Queue;
import java.util.Set;

import de.metalcon.sdd.exception.InvalidDetailException;
import de.metalcon.sdd.exception.SddException;

public class UpdateReferencingAction extends Action {

    private long nodeId;

    private Set<String> modifiedDetails;

    /* package */UpdateReferencingAction(
            Sdd sdd,
            long nodeId,
            Set<String> modifiedDetails) throws InvalidDetailException {
        super(sdd);

        if (modifiedDetails == null) {
            throw new IllegalArgumentException("modifiedDetails was null.");
        }

        for (String detail : modifiedDetails) {
            if (!config.isDetail(detail)) {
                throw new InvalidDetailException();
            }
        }

        this.nodeId = nodeId;
        this.modifiedDetails = modifiedDetails;
    }

    @Override
    public void runAction(Queue<Action> actions) throws SddException {
        sdd.actionUpdateReferencing(actions, nodeId, modifiedDetails);
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
