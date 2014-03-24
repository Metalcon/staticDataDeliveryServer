package de.metalcon.sdd.action;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class SetRelationAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relationType;

    private long toId;

    public SetRelationAction(
            long nodeId,
            String nodeType,
            String relationType,
            long toId) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relationType = relationType;
        this.toId = toId;
    }

    @Override
    public void runAction(Sdd sdd) throws SddException {
        sdd.actionSetRelation(nodeId, nodeType, relationType, toId);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // we define that two distinct SddRelationActions are never equal
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 91441;
        int mult = 463;

        hash = hash * mult + ((Long) nodeId).hashCode();
        hash = hash * mult + nodeType.hashCode();
        hash = hash * mult + relationType.hashCode();
        hash = hash * mult + ((Long) toId).hashCode();

        return hash;
    }

}
