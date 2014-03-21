package de.metalcon.sdd.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {

    public static final String LEVELDB_PATH_DEFAULT = "/usr/share/sdd/leveldb";

    public static final String NEO4J_PATH_DEFAULT = "/usr/share/sdd/neo4j";

    public static final TransactionMode TRANSACTION_MODE_DEFAULT =
            TransactionMode.SINGLE;

    private String levelDbPath = LEVELDB_PATH_DEFAULT;

    private String neo4jPath = NEO4J_PATH_DEFAULT;

    private TransactionMode transactionMode = TRANSACTION_MODE_DEFAULT;

    private Set<String> details = new HashSet<String>();

    private Map<String, ConfigNode> nodes = new HashMap<String, ConfigNode>();

    public String getLevelDbPath() {
        return levelDbPath;
    }

    public String getNeo4jPath() {
        return neo4jPath;
    }

    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    public Set<String> getDetails() {
        return details;
    }

    public boolean isDetail(String detail) {
        return details.contains(detail);
    }

    public void addDetail(String detail) {
        details.add(detail);
    }

    public Map<String, ConfigNode> getNodes() {
        return nodes;
    }

    /**
     * @return Returns the ConfigNode for type or <code>NULL</code> if no node
     *         with that type was configured.
     */
    public ConfigNode getNode(String type) {
        return nodes.get(type);
    }

    public boolean isNodeType(String type) {
        return nodes.containsKey(type);
    }

    public void addNode(String type, ConfigNode node) {
        nodes.put(type, node);
    }

    public void validate() {
    }

}
