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
        sdd.actionDeleteRelations(nodeId, nodeType, relationType, toIds);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // we define that two distinct DeleteRelationsActions are never equal
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 82378;
        int mult = 379;

        hash = hash * mult + ((Long) nodeId).hashCode();
        hash = hash * mult + nodeType.hashCode();
        hash = hash * mult + relationType.hashCode();
        hash = hash * mult + toIds.hashCode();

        return hash;
    }

}
