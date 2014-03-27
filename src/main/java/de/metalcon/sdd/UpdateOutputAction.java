package de.metalcon.sdd;

import java.util.Queue;

import de.metalcon.sdd.exception.EmptyIdException;

public class UpdateOutputAction extends Action {

    private long nodeId;

    /* package */UpdateOutputAction(
            Sdd sdd,
            long nodeId) {
        super(sdd);

        if (nodeId == Sdd.EMPTY_ID) {
            throw new EmptyIdException();
        }

        this.nodeId = nodeId;
    }

    @Override
    public void runAction(Queue<Action> actions) {
        sdd.actionUpdateOutput(actions, nodeId);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        UpdateOutputAction o = (UpdateOutputAction) other;
        return nodeId == o.nodeId;
    }

    @Override
    public int hashCode() {
        int hash = 66494;
        int mult = 877;

        hash = hash * mult + ((Long) nodeId).hashCode();

        return hash;
    }

}
