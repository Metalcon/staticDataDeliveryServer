package de.metalcon.sdd.action;

import java.util.Queue;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class AddRelationsAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relation;

    private long[] toIds;

    public AddRelationsAction(
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relation = relation;
        this.toIds = toIds;
    }

    @Override
    public void runAction(Sdd sdd, Queue<Action> actions) throws SddException {
        sdd.actionAddRelations(actions, nodeId, nodeType, relation, toIds);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // we define that two AddRelationsActions are never equal
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 77257;
        int mult = 239;

        hash = hash * mult + ((Long) nodeId).hashCode();
        hash = hash * mult + nodeType.hashCode();
        hash = hash * mult + relation.hashCode();
        hash = hash * mult + toIds.hashCode();

        return hash;
    }

}
