package de.metalcon.sdd;

import java.util.Queue;

import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.config.RelationType;

public class SetRelationAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relation;

    private long toId;

    /**
     * @param toId
     *            If this is <code>Sdd.EMPTY_ID</code>, it deletes the relation.
     */
    /* package */SetRelationAction(
            Sdd sdd,
            long nodeId,
            String nodeType,
            String relation,
            long toId) {
        super(sdd);

        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relation == null) {
            throw new IllegalArgumentException("relation was null.");
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
        if (relationType.isArray()) {
            throw ExceptionFactory.createInvalidRelationException();
        }

        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relation = relation;
        this.toId = toId;
    }

    @Override
    public void runAction(Queue<Action> actions) {
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
