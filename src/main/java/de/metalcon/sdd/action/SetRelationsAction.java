package de.metalcon.sdd.action;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class SetRelationsAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relationType;

    private long[] toIds;

    public SetRelationsAction(
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
        sdd.actionSetRelations(nodeId, nodeType, relationType, toIds);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // we define that two distinct SetRelationsAction are never equal
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 24097;
        int mult = 859;

        hash = hash * mult + ((Long) nodeId).hashCode();
        hash = hash * mult + nodeType.hashCode();
        hash = hash * mult + relationType.hashCode();
        hash = hash * mult + toIds.hashCode();

        return hash;
    }

}
