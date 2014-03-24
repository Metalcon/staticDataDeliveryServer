package de.metalcon.sdd;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import de.metalcon.sdd.action.Action;
import de.metalcon.sdd.action.AddRelationsAction;
import de.metalcon.sdd.action.DeleteAction;
import de.metalcon.sdd.action.DeleteRelationsAction;
import de.metalcon.sdd.action.SetPropertiesAction;
import de.metalcon.sdd.action.SetRelationAction;
import de.metalcon.sdd.action.SetRelationsAction;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidConfigException;

public class Sdd implements AutoCloseable {

    public static final String ID_DETAIL_DELIMITER = ":";

    public static final String ID_DELIMITER = ",";

    public static final String OUTPUT_PREFIX = "output-";

    private Config config;

    private DB outputDb;

    private TransactionalGraph nodeDb;

    private Map<Long, Vertex> nodeDbIndex;

    private Worker worker;

    private Queue<Action> transaction;

    public Sdd(
            Config config) throws InvalidConfigException, IOException {
        if (config == null) {
            throw new IllegalArgumentException("config was null.");
        }

        // config
        config.validate();
        this.config = config;

        // connect to outputDb (LevelDB)
        Options options = new Options();
        options.createIfMissing(true);
        try {
            outputDb =
                    JniDBFactory.factory.open(
                            new File(config.getLeveldbPath()), options);
        } catch (IOException e) {
            throw new IOException("Couldn't connect to outputDb (LevelDB).", e);
        }
        if (outputDb == null) {
            throw new IOException("Couldn't connect to outputDb (LevelDB).");
        }

        // connect to nodeDb (Neo4j)
        nodeDb = new Neo4jGraph(config.getNeo4jPath());
        if (nodeDb == null) {
            throw new IOException("Couldn't connect to nodeDb (Neo4j).");
        }
        // load Index
        nodeDbIndex = new HashMap<Long, Vertex>();
        for (Vertex node : nodeDb.getVertices()) {
            Long id = (Long) node.getProperty("id");
            if (id != null) {
                nodeDbIndex.put(id, node);
            }
        }

        // startup worker thread
        worker = new Worker(this);
        worker.start();

        // start with empty transaction
        transaction = new LinkedList<Action>();
    }

    @Override
    public void close() throws IOException {
        // close worker thead
        if (worker != null) {
            worker.stop();
            worker.waitForShutdown();
        }

        // close nodeDb (Neo4j)
        if (nodeDb != null) {
            nodeDb.shutdown();
        }

        // close outputDb (LevelDB)
        if (outputDb != null) {
            try {
                outputDb.close();
            } catch (IOException e) {
                throw new IOException("Couldn't close outputDb (LevelDB)", e);
            }
        }
    }

    // =========================================================================
    // PUBLIC API

    public Config getConfig() {
        return config;
    }

    public String read(long nodeId, String detail) {
        // TODO: implement read()
        return null;
    }

    public void setProperties(
            long nodeId,
            String nodeType,
            Map<String, String> properties) {
        // TODO: check call
        transaction.add(new SetPropertiesAction(nodeId, nodeType, properties));
    }

    /**
     * @param toId
     *            If this is <code>0L</code>, it deletes the relation.
     */
    public void setRelation(
            long nodeId,
            String nodeType,
            String relationType,
            long toId) {
        // TODO: check call
        transaction.add(new SetRelationAction(nodeId, nodeType, relationType,
                toId));
    }

    public void setRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        // TODO: check call
        transaction.add(new SetRelationsAction(nodeId, nodeType, relationType,
                toIds));
    }

    public void addRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        // TODO: check call
        transaction.add(new AddRelationsAction(nodeId, nodeType, relationType,
                toIds));
    }

    public void delete(long nodeId) {
        // TODO: check call
        transaction.add(new DeleteAction(nodeId));
    }

    public void deleteRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        // TODO: check call
        transaction.add(new DeleteRelationsAction(nodeId, nodeType,
                relationType, toIds));
    }

    public boolean commit() throws EmptyTransactionException {
        boolean result = worker.queueTransaction(transaction);
        transaction = new LinkedList<Action>();
        return result;
    }

    public void waitUntilQueueEmpty() {
        worker.waitUntilQueueEmpty();
    }

    // =========================================================================
    // ACTIONS

    public void actionSetProperties(
            long nodeId,
            String nodeType,
            Map<String, String> properties) {
        // TODO: implement setProperties()
        throw new UnsupportedOperationException();
    }

    /**
     * @param toId
     *            If this is <code>0L</code>, it deletes the relation.
     */
    public void actionSetRelation(
            long nodeId,
            String nodeType,
            String relationType,
            long toId) {
        // TODO: implement setRelation()
        throw new UnsupportedOperationException();
    }

    public void actionSetRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        // TODO: implement setRelations()
        throw new UnsupportedOperationException();
    }

    public void actionAddRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        // TODO: implement addRelations()
        throw new UnsupportedOperationException();
    }

    public void actionDelete(long nodeId) {
        // TODO: implement delete()
        throw new UnsupportedOperationException();
    }

    public void actionDeleteRelations(
            long nodeId,
            String nodeType,
            String relationType,
            long[] toIds) {
        // TODO: implement deleteRelations()
        throw new UnsupportedOperationException();
    }

    public void actionUpdateOutput(long nodeId) {
        // TODO: implement updateOutput()
        throw new UnsupportedOperationException();
    }

    public void
        actionUpdateReferencing(long nodeId, Set<String> modifiedDetails) {
        // TODO: implement updateReferencing()
        throw new UnsupportedOperationException();
    }

    public void actionCommit() {
        // TODO: implement commit()
        throw new UnsupportedOperationException();
    }

    private String buildIdDetail(long id, String detail) {
        return id + ID_DETAIL_DELIMITER + detail;
    }

    private String buildOutputProperty(String detail) {
        return OUTPUT_PREFIX + detail;
    }
}
