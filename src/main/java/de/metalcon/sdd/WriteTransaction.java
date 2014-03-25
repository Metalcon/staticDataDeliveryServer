package de.metalcon.sdd;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.metalcon.sdd.action.Action;
import de.metalcon.sdd.action.AddRelationsAction;
import de.metalcon.sdd.action.DeleteAction;
import de.metalcon.sdd.action.DeleteRelationsAction;
import de.metalcon.sdd.action.SetPropertiesAction;
import de.metalcon.sdd.action.SetRelationAction;
import de.metalcon.sdd.action.SetRelationsAction;
import de.metalcon.sdd.action.UpdateOutputAction;
import de.metalcon.sdd.action.UpdateReferencingAction;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.config.RelationType;
import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidDetailException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class WriteTransaction {

    private Config config;

    private Sdd sdd;

    private boolean commited = false;

    private Queue<Action> actions = new LinkedList<Action>();

    /* package */WriteTransaction(
            Sdd sdd) {
        this.sdd = sdd;
        config = sdd.getConfig();
    }

    public boolean commit() throws EmptyTransactionException,
            AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

        boolean result = sdd.commit(this);
        if (result) {
            commited = true;
        }
        return result;
    }

    public void setProperties(
            long nodeId,
            String nodeType,
            Map<String, String> properties) throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (properties == null) {
            throw new IllegalArgumentException("properties was null.");
        }

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        for (String property : properties.keySet()) {
            if (!configNode.isProperty(property)) {
                throw new InvalidPropertyException();
            }
        }

        actions.add(new SetPropertiesAction(nodeId, nodeType, properties));
    }

    /**
     * @param toId
     *            If this is <code>0L</code>, it deletes the relation.
     */
    public void setRelation(
            long nodeId,
            String nodeType,
            String relation,
            long toId) throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

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

        actions.add(new SetRelationAction(nodeId, nodeType, relation, toId));
    }

    public void setRelations(
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

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

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relation)) {
            throw new InvalidRelationException();
        }
        RelationType relationType = configNode.getRelationType(relation);
        if (!relationType.isArray()) {
            throw new InvalidRelationException();
        }

        actions.add(new SetRelationsAction(nodeId, nodeType, relation, toIds));
    }

    public void addRelations(
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) throws InvalidRelationException,
            InvalidNodeTypeException, AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

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

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relation)) {
            throw new InvalidRelationException();
        }

        actions.add(new AddRelationsAction(nodeId, nodeType, relation, toIds));
    }

    public void delete(long nodeId) throws AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

        actions.add(new DeleteAction(nodeId));
    }

    public void deleteRelations(
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) throws InvalidRelationException,
            InvalidNodeTypeException, AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

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

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relation)) {
            throw new InvalidRelationException();
        }

        actions.add(new DeleteRelationsAction(nodeId, nodeType, relation, toIds));
    }

    /* package */Queue<Action> getActions() {
        return actions;
    }

    /* package */void updateOutput(long nodeId) {
        actions.add(new UpdateOutputAction(nodeId));
    }

    /* package */void
        updateReferencing(long nodeId, Set<String> modifiedDetails)
                throws InvalidDetailException {
        if (modifiedDetails == null) {
            throw new IllegalArgumentException("modifiedDetails was null.");
        }

        for (String detail : modifiedDetails) {
            if (!config.isDetail(detail)) {
                throw new InvalidDetailException();
            }
        }

        actions.add(new UpdateReferencingAction(nodeId, modifiedDetails));
    }

}
