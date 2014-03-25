package de.metalcon.sdd;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidDetailException;

public class Sdd implements AutoCloseable {

    public static final String ID_DETAIL_DELIMITER = ":";

    public static final String ID_DELIMITER = ",";

    public static final String OUTPUT_PREFIX = "output-";

    private Config config;

    private DB outputDb;

    private TransactionalGraph nodeDb;

    private Map<Long, Vertex> nodeDbIndex;

    private Worker worker;

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

    /**
     * @return The node's output in detail or <code>NULL</code> if that node
     *         doesn't exist.
     */
    public String read(long nodeId, String detail)
            throws InvalidDetailException {
        if (detail == null) {
            throw new IllegalArgumentException("detail was null");
        }

        if (!config.isDetail(detail)) {
            throw new InvalidDetailException();
        }

        String idDetail = buildIdDetail(nodeId, detail);
        return JniDBFactory
                .asString(outputDb.get(JniDBFactory.bytes(idDetail)));
    }

    public WriteTransaction createWriteTransaction() {
        return new WriteTransaction(this);
    }

    public void waitUntilQueueEmpty() {
        worker.waitUntilQueueEmpty();
    }

    // =========================================================================
    // IMPLEMENTATION

    /* package */boolean commit(WriteTransaction transaction)
            throws EmptyTransactionException {
        return worker.queueTransaction(transaction);
    }

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
