package de.metalcon.sdd;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import de.metalcon.sdd.api.exception.InvalidNodeException;
import de.metalcon.sdd.api.exception.OutputGenerationException;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.config.ConfigNodeOutput;
import de.metalcon.sdd.config.RelationType;

public class Sdd implements AutoCloseable {

    // TODO: BUG: fix ClosedByInterruptException
    // TODO: aggregate multiple same addRelations in same transaction into one
    // TODO: generally clean up transactions?
    // TODO: implement reset property (.put("myProperty", null))
    // TODO: implement some sort of queue status

    public static final String ID_DETAIL_DELIMITER = ":";

    public static final String ID_DELIMITER = ",";

    public static final String NODEDB_ID = "sdd-id";

    public static final String NODEDB_TYPE = "sdd-type";

    public static final String NODEDB_OUTPUT_PREFIX = "sdd-output-";

    public static final String OUTPUT_ID = "id";

    public static final String OUTPUT_TYPE = "type";

    public static final boolean OUTPUT_INDENT = true;

    public static final boolean OUTPUT_ORDER = true;

    public static final long EMPTY_ID = 0L;

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        if (OUTPUT_INDENT) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        if (OUTPUT_ORDER) {
            objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        }
    }

    private Config config;

    private DB outputDb;

    private WriteBatch outputDbBatch;

    private TransactionalGraph nodeDb;

    private Map<Long, Vertex> nodeDbIndex;

    private Worker worker;

    public Sdd(
            Config config) throws IOException {
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
        outputDbBatch = null;

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
    public String read(long nodeId, String detail) {
        if (detail == null) {
            throw new IllegalArgumentException("detail was null");
        }

        if (!config.isDetail(detail)) {
            throw ExceptionFactory.createInvalidDetailException();
        }

        String idDetail = buildIdDetail(nodeId, detail);
        return JniDBFactory
                .asString(outputDb.get(JniDBFactory.bytes(idDetail)));
    }

    public WriteTransaction createWriteTransaction() {
        return new WriteTransaction(this);
    }

    public void waitUntilQueueEmpty() {
        // TODO: add timeout
        worker.waitUntilQueueEmpty();
    }

    // TODO: add waitUntilTransactionComplete(WriteTransaction transaction);

    // =========================================================================
    // IMPLEMENTATION

    /* package */boolean commit(WriteTransaction transaction) {
        return worker.queueTransaction(transaction);
    }

    /* package */void startTransaction() {
        if (outputDbBatch != null) {
            try {
                outputDbBatch.close();
            } catch (IOException e) {
                // We don't care
            }
        }
        outputDbBatch = outputDb.createWriteBatch();

        // Delete all changes since last commit, should be none if no exception
        // occurred during last transaction
        nodeDb.rollback();
    }

    /* package */void endTransaction() throws IOException {
        nodeDb.commit();

        try {
            outputDb.write(outputDbBatch);
            outputDbBatch.close();
            outputDbBatch = null;
        } catch (IOException e) {
            throw new IOException("Couldn't close OutputDB WriteBatch", e);
        }
    }

    /* package */void actionSetProperties(
            Queue<Action> actions,
            long nodeId,
            String nodeType,
            Map<String, String> properties) {
        Vertex node = getNode(actions, nodeId, nodeType, true);

        for (Map.Entry<String, String> property : properties.entrySet()) {
            // TODO: use removeProperty() if value is null?
            node.setProperty(property.getKey(), property.getValue());
        }

        actions.add(new UpdateOutputAction(this, nodeId));
    }

    /**
     * @param toId
     *            If this is <code>0L</code>, it deletes the relation.
     */
    /* package */void actionSetRelation(
            Queue<Action> actions,
            long nodeId,
            String nodeType,
            String relation,
            long toId) {
        // TODO: only remove edges that are not toId, and only create new edge
        // if needed

        Vertex node = getNode(actions, nodeId, nodeType, true);
        for (Edge edge : node.getEdges(Direction.OUT, relation)) {
            edge.remove();
        }

        ConfigNode configNode = config.getNode(nodeType);
        RelationType relationType = configNode.getRelationType(relation);

        Vertex relNode = getNode(actions, toId, relationType.getType(), true);
        nodeDb.addEdge(null, node, relNode, relation);

        actions.add(new UpdateOutputAction(this, nodeId));
    }

    /* package */void actionSetRelations(
            Queue<Action> actions,
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) {
        // TODO: only remove edges that are not in toIds, and only create
        // remaining needed edges

        Vertex node = getNode(actions, nodeId, nodeType, true);
        for (Edge edge : node.getEdges(Direction.OUT, relation)) {
            edge.remove();
        }

        actionAddRelations(actions, nodeId, nodeType, relation, toIds);
    }

    /* package */void actionAddRelations(
            Queue<Action> actions,
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) {
        Vertex node = getNode(actions, nodeId, nodeType, true);

        ConfigNode configNode = config.getNode(nodeType);
        RelationType relationType = configNode.getRelationType(relation);

        for (long toId : toIds) {
            Vertex relNode =
                    getNode(actions, toId, relationType.getType(), true);
            nodeDb.addEdge(null, node, relNode, relation);
        }

        actions.add(new UpdateOutputAction(this, nodeId));
    }

    /* package */void actionDelete(Queue<Action> actions, long nodeId) {
        // TODO: implement delete()
        throw new UnsupportedOperationException();
    }

    /* package */void actionDeleteRelations(
            Queue<Action> actions,
            long nodeId,
            String nodeType,
            String relation,
            long[] toIds) {
        // TODO: implement deleteRelations()
        throw new UnsupportedOperationException();
    }

    /* package */void actionUpdateOutput(Queue<Action> actions, long nodeId) {
        // TODO: move vertex into parameters to avoid lookup?
        Vertex node = getNode(actions, nodeId);
        Set<String> modifiedDetails = new HashSet<String>();

        for (String detail : config.getDetails()) {
            String idDetail = buildIdDetail(nodeId, detail);

            String output = generateOutput(node, nodeId, detail);
            String oldOutput = node.getProperty(buildOutputProperty(detail));

            if (!output.equals(oldOutput)) {
                modifiedDetails.add(detail);
                node.setProperty(buildOutputProperty(detail), output);
                outputDbBatch.put(JniDBFactory.bytes(idDetail),
                        JniDBFactory.bytes(output));
            }
        }

        actions.add(new UpdateReferencingAction(this, nodeId, modifiedDetails));
    }

    /* package */void actionUpdateReferencing(
            Queue<Action> actions,
            long nodeId,
            Set<String> modifiedDetails) {
        // TODO: move vertex into parameters to avoid lookup?
        Vertex node = getNode(actions, nodeId);
        String nodeType = getNodeType(node);

        for (Vertex referencingNode : node.getVertices(Direction.IN)) {
            long referencingNodeId = getNodeId(referencingNode);
            String referencingNodeType = getNodeType(referencingNode);
            ConfigNode configNode = config.getNode(referencingNodeType);

            if (configNode.dependsOn(nodeType, modifiedDetails)) {
                actions.add(new UpdateOutputAction(this, referencingNodeId));
            }
        }
    }

    private static String buildIdDetail(long nodeId, String detail) {
        return nodeId + ID_DETAIL_DELIMITER + detail;
    }

    private static String buildOutputProperty(String detail) {
        return NODEDB_OUTPUT_PREFIX + detail;
    }

    private Vertex getNode(Queue<Action> actions, long nodeId) {
        return getNode(actions, nodeId, null, false);
    }

    private Vertex getNode(
            Queue<Action> actions,
            long nodeId,
            String nodeType,
            boolean create) {
        Vertex node = nodeDbIndex.get(nodeId);
        if (node != null) {
            String type = getNodeType(node);
            if (type == null || (nodeType != null && !nodeType.equals(type))) {
                throw new InvalidNodeException();
            }
            return node;
        }

        if (!create || nodeType == null) {
            throw new InvalidNodeException();
        }

        if (!config.isNodeType(nodeType)) {
            throw ExceptionFactory.createInvalidNodeTypeException();
        }

        node = nodeDb.addVertex(null);
        node.setProperty(NODEDB_ID, nodeId);
        node.setProperty(NODEDB_TYPE, nodeType);
        nodeDbIndex.put(nodeId, node);

        actions.add(new UpdateOutputAction(this, nodeId));

        return node;
    }

    private long getNodeId(Vertex node) {
        Long nodeId = node.getProperty(NODEDB_ID);
        if (nodeId == null) {
            throw new InvalidNodeException();
        }
        return nodeId;
    }

    private String getNodeType(Vertex node) {
        String type = node.getProperty(NODEDB_TYPE);
        if (type == null || !config.isNodeType(type)) {
            throw new InvalidNodeException();
        }
        return type;
    }

    private String generateOutput(Vertex node, long nodeId, String detail) {
        try {
            String nodeType = getNodeType(node);

            ConfigNode configNode = config.getNode(nodeType);
            ConfigNodeOutput configNodeOutput = configNode.getOutput(detail);

            Map<String, Object> output = new HashMap<String, Object>();
            output.put(OUTPUT_ID, nodeId);
            output.put(OUTPUT_TYPE, nodeType);

            if (configNodeOutput != null) {
                for (String property : configNodeOutput.getOutProperties()) {
                    String propertyValue = node.getProperty(property);
                    output.put(property, propertyValue);
                }

                for (String relation : configNodeOutput.getOutRelations()) {
                    RelationType relationType =
                            configNode.getRelationType(relation);
                    String relationDetail =
                            configNodeOutput.getOutRelationDetail(relation);
                    Object relOutput = null;

                    Iterable<Vertex> relNodes =
                            node.getVertices(Direction.OUT, relation);
                    if (!relationType.isArray()) {
                        if (relNodes.iterator().hasNext()) {
                            Vertex relNode = relNodes.iterator().next();
                            String relNodeOutput =
                                    relNode.getProperty(buildOutputProperty(relationDetail));
                            // If relNodeOutput isn't set yet, it should be
                            // generated through UpdateReferencingActions
                            if (relNodeOutput != null) {
                                relOutput =
                                        objectMapper.readTree(relNodeOutput);
                            }
                        }
                    } else {
                        List<JsonNode> relOutputs = new LinkedList<JsonNode>();
                        for (Vertex relNode : relNodes) {
                            String relNodeOutput =
                                    relNode.getProperty(buildOutputProperty(relationDetail));
                            // If relNodeOutput isn't set yet, it should be
                            // generated through UpdateReferencingActions
                            if (relNodeOutput != null) {
                                relOutputs.add(objectMapper
                                        .readTree(relNodeOutput));
                            }
                            // System.out.println(relNodeOutput);
                        }
                        // System.out.println("\n\n###\n\n");
                        if (!relOutputs.isEmpty()) {
                            relOutput = relOutputs;
                        }
                    }

                    output.put(relation, relOutput);
                }
            }

            return objectMapper.writeValueAsString(output);
        } catch (IOException e) {
            throw new OutputGenerationException(e);
        }
    }

}
