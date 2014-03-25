package de.metalcon.sdd;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.metalcon.sdd.action.Action;
import de.metalcon.sdd.action.AddRelationsAction;
import de.metalcon.sdd.action.DeleteAction;
import de.metalcon.sdd.action.DeleteRelationsAction;
import de.metalcon.sdd.action.SetPropertiesAction;
import de.metalcon.sdd.action.SetRelationAction;
import de.metalcon.sdd.action.SetRelationsAction;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class WriteTransaction {

    private Config config;

    private Sdd sdd;

    private List<Action> actions = new LinkedList<Action>();

    /* package */WriteTransaction(
            Sdd sdd) {
        this.sdd = sdd;
        config = sdd.getConfig();
    }

    /* package */List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public boolean commit() throws EmptyTransactionException {
        return sdd.commit(this);
    }

    public void setProperties(
            long nodeId,
            String nodeType,
            Map<String, String> properties) throws InvalidNodeTypeException,
            InvalidPropertyException {
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
            String relationType,
            long toId) throws InvalidNodeTypeException,
            InvalidRelationException {
        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("relationType was null.");
        }

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relationType)) {
            throw new InvalidRelationException();
        }

        actions.add(new SetRelationAction(nodeId, nodeType, relationType, toId));
    }

    public void setRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) throws InvalidNodeTypeException,
            InvalidRelationException {
        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("relationType was null.");
        }
        if (toIds == null) {
            throw new IllegalArgumentException("toIds was null.");
        }

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relationType)) {
            throw new InvalidRelationException();
        }

        actions.add(new SetRelationsAction(nodeId, nodeType, relationType,
                toIds));
    }

    public void addRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) throws InvalidRelationException,
            InvalidNodeTypeException {
        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("relationType was null.");
        }
        if (toIds == null) {
            throw new IllegalArgumentException("toIds was null.");
        }

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relationType)) {
            throw new InvalidRelationException();
        }

        actions.add(new AddRelationsAction(nodeId, nodeType, relationType,
                toIds));
    }

    public void delete(long nodeId) {
        actions.add(new DeleteAction(nodeId));
    }

    public void deleteRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) throws InvalidRelationException,
            InvalidNodeTypeException {
        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("relationType was null.");
        }
        if (toIds == null) {
            throw new IllegalArgumentException("toIds was null.");
        }

        if (!config.isNodeType(nodeType)) {
            throw new InvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (!configNode.isRelation(relationType)) {
            throw new InvalidRelationException();
        }

        actions.add(new DeleteRelationsAction(nodeId, nodeType, relationType,
                toIds));
    }

}
