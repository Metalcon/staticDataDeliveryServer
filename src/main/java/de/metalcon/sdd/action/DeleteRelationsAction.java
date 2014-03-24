package de.metalcon.sdd.action;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class DeleteRelationsAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relationType;

    private long[] toIds;

    public DeleteRelationsAction(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relationType = relationType;
        this.toIds = toIds;
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
