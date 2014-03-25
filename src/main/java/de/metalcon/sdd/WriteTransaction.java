package de.metalcon.sdd;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidDetailException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class WriteTransaction {

    private Sdd sdd;

    private boolean commited = false;

    private Queue<Action> actions = new LinkedList<Action>();

    /* package */WriteTransaction(
            Sdd sdd) {
        this.sdd = sdd;
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

        actions.add(new SetPropertiesAction(sdd, nodeId, nodeType, properties));
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

        actions.add(new SetRelationAction(sdd, nodeId, nodeType, relation, toId));
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

        actions.add(new SetRelationsAction(sdd, nodeId, nodeType, relation,
                toIds));
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

        actions.add(new AddRelationsAction(sdd, nodeId, nodeType, relation,
                toIds));
    }

    public void delete(long nodeId) throws AlreadyCommitedException {
        if (commited) {
            throw new AlreadyCommitedException();
        }

        actions.add(new DeleteAction(sdd, nodeId));
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

        actions.add(new DeleteRelationsAction(sdd, nodeId, nodeType, relation,
                toIds));
    }

    /* package */Queue<Action> getActions() {
        return actions;
    }

    /* package */void updateOutput(long nodeId) {
        actions.add(new UpdateOutputAction(sdd, nodeId));
    }

    /* package */void
        updateReferencing(long nodeId, Set<String> modifiedDetails)
                throws InvalidDetailException {
        actions.add(new UpdateReferencingAction(sdd, nodeId, modifiedDetails));
    }

}
