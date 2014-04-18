package de.metalcon.sdd;

import java.util.Queue;

import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.config.RelationType;

public class SetRelationsAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relation;

    private long[] toIds;

    /* package */SetRelationsAction(
            Sdd sdd,
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) {
        super(sdd);

        // TODO: check duplicates

        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relation == null) {
            throw new IllegalArgumentException("relation was null.");
        }
        if (toIds == null) {
            throw new IllegalArgumentException("toIds was null.");
        }

        if (nodeId == Sdd.EMPTY_ID) {
            throw ExceptionFactory.createEmptyIdException();
        }
        if (!config.isNodeType(nodeType)) {
            throw ExceptionFactory.createInvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relation)) {
            throw ExceptionFactory.createInvalidRelationException();
        }
        RelationType relationType = configNode.getRelationType(relation);
        if (!relationType.isArray()) {
            throw ExceptionFactory.createInvalidRelationException();
        }
        for (long toId : toIds) {
            if (toId == Sdd.EMPTY_ID) {
                throw ExceptionFactory.createEmptyIdException();
            }
        }

        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relation = relation;
        this.toIds = toIds;
    }

    @Override
    public void runAction(Queue<Action> actions) {
        sdd.actionSetRelations(actions, nodeId, nodeType, relation, toIds);
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
        hash = hash * mult + relation.hashCode();
        hash = hash * mult + toIds.hashCode();

        return hash;
    }

}
