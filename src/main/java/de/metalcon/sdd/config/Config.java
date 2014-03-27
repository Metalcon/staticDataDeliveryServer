package de.metalcon.sdd.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.metalcon.sdd.exception.InvalidConfigException;

public class Config {

    public static final String LEVELDB_PATH_DEFAULT = "/usr/share/sdd/leveldb";

    public static final String NEO4J_PATH_DEFAULT = "/usr/share/sdd/neo4j";

    public static final TransactionMode TRANSACTION_MODE_DEFAULT =
            TransactionMode.SINGLE;

    private String leveldbPath = LEVELDB_PATH_DEFAULT;

    private String neo4jPath = NEO4J_PATH_DEFAULT;

    private TransactionMode transactionMode = TRANSACTION_MODE_DEFAULT;

    private Set<String> details = new HashSet<String>();

    private Map<String, ConfigNode> nodes = new HashMap<String, ConfigNode>();

    public String getLeveldbPath() {
        return leveldbPath;
    }

    public void setLeveldbPath(String leveldbPath) {
        if (leveldbPath == null) {
            throw new IllegalArgumentException("leveldbPath was null.");
        }
        this.leveldbPath = leveldbPath;
    }

    public String getNeo4jPath() {
        return neo4jPath;
    }

    public void setNeo4jPath(String neo4jPath) {
        if (neo4jPath == null) {
            throw new IllegalArgumentException("neo4jPath was null.");
        }
        this.neo4jPath = neo4jPath;
    }

    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(TransactionMode transactionMode) {
        if (transactionMode == null) {
            throw new IllegalArgumentException("transactionMode was null");
        }
        this.transactionMode = transactionMode;
    }

    public Set<String> getDetails() {
        return Collections.unmodifiableSet(details);
    }

    public boolean isDetail(String detail) {
        if (detail == null) {
            throw new IllegalArgumentException("detail was null.");
        }
        return details.contains(detail);
    }

    public void addDetail(String detail) {
        if (detail == null) {
            throw new IllegalArgumentException("detail was null.");
        }
        if (isDetail(detail)) {
            throw new InvalidConfigException("Duplicate detail definition: \""
                    + detail + "\".");
        }
        details.add(detail);
    }

    public Set<String> getNodeTypes() {
        return Collections.unmodifiableSet(nodes.keySet());
    }

    public boolean isNodeType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type was null.");
        }
        return nodes.containsKey(type);
    }

    /**
     * @return The ConfigNode for type or <code>NULL</code> if no node with that
     *         type was configured.
     */
    public ConfigNode getNode(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type was null.");
        }
        return nodes.get(type);
    }

    public void addNode(String type, ConfigNode node) {
        if (type == null) {
            throw new IllegalArgumentException("type was null.");
        }
        if (node == null) {
            throw new IllegalArgumentException("node was null.");
        }
        if (isNodeType(type)) {
            throw new InvalidConfigException("Duplicate node definition: \""
                    + type + "\".");
        }
        nodes.put(type, node);
    }

    /**
     * Checks if the Config is a valid SDD configuration.
     * 
     * Remains silent if valid, throws on error.
     * 
     * These cases are checked:
     * <ul>
     * <li>Do all relations reference existing node types?</li>
     * <li>Are all defined output details for nodes actually valid details?</li>
     * <li>Do all out-relations define a valid output detail?</li>
     * </ul>
     */
    public void validate() {
        // TODO: Check whether we have write access on paths.

        // Check nodes
        for (String nodeType : getNodeTypes()) {
            ConfigNode node = getNode(nodeType);

            // Check relations
            for (String relation : node.getRelations()) {
                RelationType relationType = node.getRelationType(relation);

                if (!isNodeType(relationType.getType())) {
                    throw new InvalidConfigException(
                            "Invalid Relation type for \"" + relation
                                    + "\" on node \"" + nodeType + "\": \""
                                    + relationType.getType() + "\".");
                }
            }

            // Check outputs
            for (String detail : node.getOutputDetails()) {
                ConfigNodeOutput output = node.getOutput(detail);

                if (!isDetail(detail)) {
                    throw new InvalidConfigException("Invalid output detail \""
                            + detail + "\" on node \"" + nodeType + "\".");
                }

                // Check out-relations
                for (String outRelation : output.getOutRelations()) {
                    String outRelationDetail =
                            output.getOutRelationDetail(outRelation);

                    if (node.getRelationType(outRelation) == null) {
                        throw new InvalidConfigException("Invalid relation \""
                                + outRelation + " for out-relation on node \""
                                + nodeType + "\".");
                    }

                    if (!isDetail(outRelationDetail)) {
                        throw new InvalidConfigException("Invalid detail \""
                                + outRelationDetail + "\" for out-relation \""
                                + outRelation + "\" on node \"" + node + "\".");
                    }
                }
            }
        }
    }

    public void makeTemporary() throws IOException {
        Path tmpDir = Files.createTempDirectory("sddTemp");

        Path leveldbPath = tmpDir.resolve("leveldb");
        leveldbPath.toFile().mkdir();
        setLeveldbPath(leveldbPath.toString());

        Path neo4jPath = tmpDir.resolve("neo4j");
        neo4jPath.toFile().mkdir();
        setNeo4jPath(neo4jPath.toString());
    }
}
