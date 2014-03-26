package de.metalcon.sdd;

import java.util.Queue;

import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.config.RelationType;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidRelationException;
import de.metalcon.sdd.exception.SddException;

public class SetRelationAction extends Action {

    private long nodeId;

    private String nodeType;

    private String relation;

    private long toId;

    /**
     * @param toId
     *            If this is <code>0L</code>, it deletes the relation.
     */
    /* package */SetRelationAction(
            Sdd sdd,
            long nodeId,
            String nodeType,
            String relation,
            long toId) throws InvalidRelationException,
            InvalidNodeTypeException {
        super(sdd);

        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relation == null) {
            throw new IllegalArgumentException("relation was null.");
        }

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }

        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relation)) {
            throw new InvalidRelationException();
        }
        RelationType relationType = configNode.getRelationType(relation);
        if (relationType.isArray()) {
            throw new InvalidRelationException();
        }

        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.relation = relation;
        this.toId = toId;
    }

    @Override
    public void runAction(Queue<Action> actions) throws SddException {
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
