package de.metalcon.sdd.action;

import java.util.Queue;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class SetRelationAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relation;

    private long toId;

    public SetRelationAction(
            long nodeId,
            String nodeType,
            String relation,
            long toId) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relation = relation;
        this.toId = toId;
    }

    @Override
    public void runAction(Sdd sdd, Queue<Action> actions) throws SddException {
        sdd.actionSetRelation(actions, nodeId, nodeType, relation, toId);
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
        hash = hash * mult + relation.hashCode();
        hash = hash * mult + ((Long) toId).hashCode();

        return hash;
    }

}
