package de.metalcon.sdd.action;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class DeleteAction extends Action {

    private long nodeId;

    public DeleteAction(
            long nodeId) {
        this.nodeId = nodeId;
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

        // we define that two distinct DeleteActions are never equal
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 47220;
        int mult = 421;

        hash = hash * mult + ((Long) nodeId).hashCode();

        return hash;
    }

}
